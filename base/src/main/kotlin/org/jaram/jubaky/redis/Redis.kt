package org.jaram.jubaky.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.support.ConnectionPoolSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import java.io.Closeable


class Redis(
    private val host: String,
    private val port: Int,
    private val dbIndex: Int = 0,
    private val keyPrefix: String = ""
) : Closeable {

    private val uri = RedisURI.Builder.redis(host, port).withDatabase(dbIndex).build()
    private val client = RedisClient.create(uri)
    private val poolConfig = GenericObjectPoolConfig<RedisClient>().apply {
        testOnBorrow = true
        minIdle = 1
        maxIdle = 16
        maxTotal = 16
        maxWaitMillis = 5000
        testWhileIdle = true
        timeBetweenEvictionRunsMillis = 300000
    }
    private val pool = ConnectionPoolSupport.createGenericObjectPool({ client.connect() }, poolConfig)

    override fun close() {
        pool.close()
        client.shutdown()
    }

    suspend fun ping() = execute { ping() }

    suspend fun set(key: String, value: String, expire: Int? = null) {
        execute {
            set(keyPrefix + key, value)

            if (expire != null) {
                expire(keyPrefix + key, expire.toLong())
            }
        }
    }

    suspend fun get(key: String): String? {
        return read { get(keyPrefix + key) }
    }

    suspend fun set(vararg pairs: Pair<String, String>, expire: Int? = null) {
        execute {
            val data = pairs.map { Pair(keyPrefix + it.first, it.second) }

            mset(data.toMap())

            data.forEach {
                if (expire != null) {
                    expire(it.first, expire.toLong())
                }
            }
        }
    }

    suspend fun get(vararg keys: String): Map<String, String?> {
        return read {
            val realKeys = keys.map { keyPrefix + it }

            val response = mget(*realKeys.toTypedArray())

            response.map { Pair(it.key, it.value) }.toMap()
        }
    }

    suspend fun getAndSet(key: String, value: String): String? {
        return execute { getset(keyPrefix + key, value) }
    }

    suspend fun delete(vararg key: String) {
        execute { key.forEach { del(it) } }
    }

    suspend fun lpush(key: String, vararg value: String) {
        execute { lpush(key, *value); }
    }

    suspend fun lrange(key: String, start: Long, end: Long): List<String> {
        return read { lrange(key, start, end) }
    }

    suspend fun lpop(key: String): String {
        return execute { lpop(key) }
    }

    private suspend fun <T> execute(statement: RedisCommands<String, String>.() -> T): T = withContext(Dispatchers.IO) {
        val connection = pool.borrowObject()
        val commands = connection.sync()
        val result = try {
            statement(commands)
        } catch (e: Exception) {
            pool.invalidateObject(connection)
            throw e
        }

        pool.returnObject(connection)

        result
    }

    private suspend fun <T> read(statement: RedisCommands<String, String>.() -> T): T = execute(statement)
}
package org.jaram.jubaky.module

import com.zaxxer.hikari.HikariDataSource
import org.jaram.jubaky.db.DB
import org.jaram.jubaky.ext.getString
import org.jaram.jubaky.ext.minuteToSecond
import org.jaram.jubaky.ext.secondToMillisecond
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onRelease

val DataSourceModule = module {
    single(named("jubaky-db")) {
        DB(
            get(named("jubaky-datasource")),
            get(named("jubaky-datasource"))
        )
    }

    single(named("jubaky-datasource"), createdAtStart = true) {
        HikariDataSource().apply {
            driverClassName = getString("db.jubaky.driver")
            jdbcUrl = getString("db.jubaky.url")
            username = getString("db.jubaky.username")
            password = getString("db.jubaky.password")
            connectionTimeout = 5.secondToMillisecond().toLong()
            maxLifetime = 15.minuteToSecond().secondToMillisecond().toLong()
            connectionTestQuery = "SELECT 1"
            poolName = "jubaky"

            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            addDataSourceProperty("useUnicode", "true")
            addDataSourceProperty("characterEncoding", "utf8")
        }
    }.onRelease { it?.close() }
}
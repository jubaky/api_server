package org.jaram.jubaky.db.dao

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Tokens
import org.jaram.jubaky.db.table.Users
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

class UserDao(
    private val db: DB
) {
    suspend fun createUser(emailId: String, password: ByteArray, name: String) {
        db.execute {
            Users.insert {
                it[this.emailId] = emailId
                it[this.password] = password
                it[this.name] = name
                it[this.createTime] = DateTime.now()
            }
        }
    }

    suspend fun removeUser(emailId: String, password: ByteArray) {
        db.execute {
            Users.deleteWhere {
                Users.emailId.eq(emailId) and Users.password.eq(password)
            }
            Tokens.deleteWhere {
                Tokens.emailId.eq(emailId)
            }
        }
    }

    /*
    suspend fun userExists(emailId: String): exists {
        return exists(Users.select(Users.emailId.eq(emailId)))
    }
    */

    suspend fun isUser(emailId: String, password: ByteArray): Boolean {
        var isUser: Boolean = false
        db.read {
            Users.slice(Users.emailId, Users.password).select {
                Users.emailId.eq(emailId) and Users.password.eq(password)
            }.forEach {
                if (it[Users.emailId] != null && it[Users.password] != null) {
                    // exits
                    isUser = true
                } else {
                    // no exits
                    println("No email or password")
                    isUser = false
                }
            }
        }
        return isUser
    }

    suspend fun setLastLoginTime(emailId: String) {
        db.execute {
            Users.select {
                Users.emailId.eq(emailId)
            }.forEach {
                Users.insert {
                    it[this.lastLoginTime] = DateTime.now()
                }
            }
        }
    }

    suspend fun storeToken(token: String, emailId: String, expiredTime: DateTime) {
        db.execute {
            Users.select {
                Users.emailId.eq(emailId)
            }.forEach {
                Tokens.insert {
                    it[this.emailId] = emailId
                    it[this.token] = token
                    it[this.expiredTime] = expiredTime
                }
            }
        }
    }

    suspend fun removeToken(emailId: String) {
        db.execute {
            Tokens.deleteWhere {
                Tokens.emailId.eq(emailId)
            }
        }
    }

    private val jwtIssuer: String = "jubaky.org"
    /* TODO: create new secret key */
    private val secret: String = ""
    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
    private val validityInMs = 3_600_000 * 10 // 10 hours
    private val jwtAudience: String = "jubaky-audience"

    fun generateToken(emailId: String, name: String): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(jwtIssuer)
            .withAudience(jwtAudience)
            .withClaim("name", name)
            .withExpiresAt(getExpiration().toDate())
            .sign(algorithm)
    }

    fun getExpiration() = DateTime(System.currentTimeMillis() + validityInMs)
}
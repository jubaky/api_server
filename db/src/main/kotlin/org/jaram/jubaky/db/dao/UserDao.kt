package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
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
        }
    }

    suspend fun doubleCheckEmailId(emailId: String): Boolean = db.read {
        Users.slice(Users.emailId).select {
            Users.emailId.eq(emailId)
        }.empty()
    }

    suspend fun isUser(emailId: String, password: ByteArray): Boolean = db.read {
        Users.slice(Users.emailId, Users.password).select {
            Users.emailId.eq(emailId) and Users.password.eq(password)
        }.empty()
    }

    suspend fun updateLastLoginTime(emailId: String) {
        db.execute {
            Users.update({ Users.emailId.eq(emailId) }) {
                it[this.lastLoginTime] = DateTime.now()
            }
        }
    }
}
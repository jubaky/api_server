package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Users
import org.jetbrains.exposed.sql.insert

class UserDao(
    private val db: DB
) {

    suspend fun createUser(emailId: String, password: ByteArray, name: String) {
        db.execute {
            Users.insert {
                it[this.emailId] = emailId
                it[this.password] = password
                it[this.name] = name
            }
        }
    }
}
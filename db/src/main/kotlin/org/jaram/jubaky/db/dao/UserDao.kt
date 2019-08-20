package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.GroupMembers
import org.jaram.jubaky.db.table.Groups
import org.jaram.jubaky.db.table.Users
import org.jaram.jubaky.domain.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class UserDao(private val db: DB) {

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

    suspend fun updateLastLoginTime(emailId: String, time: DateTime) {
        db.execute {
            Users.update(
                where = { Users.emailId eq emailId },
                body = {
                    it[this.lastLoginTime] = time
                }
            )
        }
    }

    suspend fun disableUser(userId: Int) {
        db.execute {
            val emailId = Users.slice(Users.emailId).select { Users.id eq userId }.first()

            Users.update(
                where = { Users.id eq userId },
                body = {
                    it[this.emailId] = "deactivate-${System.currentTimeMillis()}-$emailId"
                    it[this.password] = byteArrayOf()
                    it[this.name] = ""
                    it[this.isDisabled] = true
                }
            )
        }
    }

    suspend fun disableUser(emailId: String) {
        db.execute {
            Users.update(
                where = { Users.emailId eq emailId },
                body = {
                    it[this.emailId] = "deactivate-${System.currentTimeMillis()}-$emailId"
                    it[this.password] = byteArrayOf()
                    it[this.name] = ""
                    it[this.isDisabled] = true
                }
            )
        }
    }

    suspend fun isDuplicatedEmail(emailId: String) = db.read {
        !Users.slice(Users.emailId).select {
            Users.emailId.eq(emailId)
        }.empty()
    }

    suspend fun isValidCredentials(emailId: String, password: ByteArray) = db.read {
        Users.slice(Users.password).select {
            (Users.isDisabled eq false)
                .and(Users.emailId eq emailId)
        }.firstOrNull()?.get(Users.password)?.contentEquals(password) == true
    }

    suspend fun getUserInfo(emailId: String): User = db.read {
        Users.innerJoin(GroupMembers).innerJoin(Groups).select {
            Users.emailId.eq(emailId)
        }.map {
            User (
                emailId = it[Users.emailId],
                password = it[Users.password],
                name = it[Users.name],
                groupName = it[Groups.name]
            )
        }.first()
    }

    suspend fun getUserGroupId(groupName: String): Int = db.read {
        Groups.slice(Groups.id).select { Groups.name.eq(groupName) }.map { it[Groups.id].value }.first()
    }
}
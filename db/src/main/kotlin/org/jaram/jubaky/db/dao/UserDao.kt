package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.GroupMembers
import org.jaram.jubaky.db.table.Groups
import org.jaram.jubaky.db.table.Users
import org.jaram.jubaky.domain.User
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.security.MessageDigest

class UserDao(private val db: DB) {

    suspend fun createUser(emailId: String, password: ByteArray, name: String) {
        db.execute {
            Users.insert {
                it[this.emailId] = emailId
                it[this.password] = hashPassword(password)
                it[this.name] = name
            }
        }
    }

    private fun hashPassword(password: ByteArray): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest.getInstance("SHA-256").digest(password)
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()

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
                    it[this.password] = ""
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
                    it[this.password] = ""
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

    suspend fun isValidCredentials(emailId: String, password: ByteArray): Boolean = db.read {
        Users.slice(Users.password).select {
            Users.isDisabled.eq(false) and Users.emailId.eq(emailId)
        }.firstOrNull()?.get(Users.password)?.contentEquals(hashPassword(password)) == true
    }

    suspend fun getUserInfo(userId: Int): User = db.read {
        Users.innerJoin(GroupMembers).innerJoin(Groups).select {
            Users.id.eq(userId)
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

    suspend fun getUserId(emailId: String): Int = db.read {
        Users.select {
            Users.emailId.eq(emailId)
        }.first()[Users.id].value
    }

    suspend fun registerGroupMember(groupId: Int, userId: Int) {
        db.execute{
            GroupMembers.insert {
                it[this.groupId] = EntityID(groupId, Groups)
                it[this.userId] = EntityID(userId, Users)
            }
        }
    }
}
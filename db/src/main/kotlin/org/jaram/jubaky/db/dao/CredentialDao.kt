package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Credentials
import org.jaram.jubaky.db.table.Users
import org.jaram.jubaky.protocol.CredentialInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class CredentialDao(private val db: DB) {
//    suspend fun getCredentialList(userId: Int): List<CredentialInfo> = db.read {
//        Credentials.innerJoin(Users).select {
//            Users.id.eq(userId)
//        }.map {
//            CredentialInfo (
//                id = it[Users.id].value,
//                userName = it[Credentials.userName],
//                password = it[Credentials.password],
//                key = it[Credentials.key]
//            )
//        }
//    }
//
//    suspend fun createCredential(userId: Int, userName: String, password: String, key: String) {
//        db.execute {
//            Credentials.insert {
//                it[this.userId] = EntityID(userId, Users)
//                it[this.userName] = userName
//                it[this.password] = password
//                it[this.key] = key
//            }
//        }
//    }

    suspend fun getCredentialList(): List<CredentialInfo> = db.read {
        Credentials.selectAll().map {
            CredentialInfo (
                userName = it[Credentials.userName],
                password = it[Credentials.password],
                key = it[Credentials.key]
            )
        }
    }

    suspend fun createCredential(userName: String, password: String, key: String) {
        db.execute {
            Credentials.insert {
                it[this.userName] = userName
                it[this.password] = password
                it[this.key] = key
            }
        }
    }

    suspend fun deleteCredential(key: String) {
        db.execute {
            Credentials.deleteWhere { Credentials.key.eq(key) }
        }
    }

}
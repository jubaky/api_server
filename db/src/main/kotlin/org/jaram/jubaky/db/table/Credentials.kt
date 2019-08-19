package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable

object Credentials: IntIdTable("Credentials") {
    val user = reference("user_id", Users)
    val userName = varchar("user_name", 30)
    val password = varchar("password", 128)
    val key = varchar("key", 256)
}
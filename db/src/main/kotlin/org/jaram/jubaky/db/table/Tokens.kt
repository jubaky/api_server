package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Tokens : IntIdTable("token") {
    val emailId: Column<String> = varchar("email_id", 256).references(Users.emailId)
    val token: Column<String> = varchar("uuid", 256).uniqueIndex()
    val expiredTime: Column<DateTime?> = datetime("expired_time").nullable()
}
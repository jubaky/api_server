package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Users : IntIdTable("user") {
    val emailId: Column<String> = varchar("email_id", 256).uniqueIndex()
    val password: Column<ByteArray> = binary("password", 128)
    val name: Column<String> = varchar("name", 256)
    val createTime: Column<DateTime> = datetime("create_time").default(DateTime.now())
    val lastLoginTime: Column<DateTime?> = datetime("last_login_time").nullable()
    val isDisabled: Column<Boolean> = bool("is_disable").default(false)
}
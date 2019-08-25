package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object Users : IntIdTable("Users") {
    val emailId = varchar("email_id", 256).uniqueIndex()
    val password = varchar("password", 128)
    val name = varchar("name", 30)
    val createTime = datetime("create_time").default(DateTime.now())
    val lastLoginTime = datetime("last_login_time").nullable()
    val isDisabled = bool("is_disable").default(false)
}
package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Project: IntIdTable("project") {
    val name: Column<String> = varchar("name", 256).uniqueIndex()
    val repositoryAddr: Column<String> = varchar("repository_addr", 256)
    val createTime: Column<DateTime> = datetime("create_time").default(DateTime.now())
    val modifyTime: Column<DateTime> = datetime("modify_time")
}
package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Applications: IntIdTable("application") {
    val name = varchar("name", 32).uniqueIndex()
    val repositoryAddr = varchar("repository_addr", 256)
    val createTime = datetime("create_time").default(DateTime.now())
    val updateTime = datetime("update_time").default(DateTime.now())
}
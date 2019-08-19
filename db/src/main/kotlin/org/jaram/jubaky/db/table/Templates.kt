package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object Templates : IntIdTable("Templates") {
    val name = varchar("name", 64)
    val kind = varchar("kind", 20)
    val content = text("content")
    val application = reference("project_id", Applications)
    val createTime = datetime("create_time").default(DateTime.now())
    val updateTime = datetime("update_time").nullable()
}
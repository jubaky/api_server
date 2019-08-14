package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Template: IntIdTable("template") {
    val name: Column<String> = varchar("name", 256).uniqueIndex()
    val objectConfigFile: Column<String> = text("object_config_file")
    val projectId: Column<Int> = integer("project_id").references(Project.id)
    val createTime: Column<DateTime> = datetime("create_time").default(DateTime.now())
    val modifyTime: Column<DateTime> = datetime("modify_time")
}
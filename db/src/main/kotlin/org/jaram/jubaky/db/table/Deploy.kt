package org.jaram.jubaky.db.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

object Deploy: Table() {
    val buildId: Column<Int> = integer("build_id").references(Build.id).primaryKey()
    val templateId: Column<Int> = integer("template_id").references(Template.id).primaryKey()
    val startTime: Column<DateTime> = datetime("start_time").default(DateTime.now())
    val endTime: Column<DateTime?> = datetime("end_time").nullable()
}
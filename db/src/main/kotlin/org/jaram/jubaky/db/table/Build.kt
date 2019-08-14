package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Build: IntIdTable("build") {
    val buildResult: Column<String?> = text("build_result").nullable()
    val tag: Column<String> = varchar("tag", 256)
    val registerTime: Column<DateTime> = datetime("register_time").default(DateTime.now())
    val startTime: Column<DateTime> = datetime("start_time")
    val endTime: Column<DateTime> = datetime("end_time")
}
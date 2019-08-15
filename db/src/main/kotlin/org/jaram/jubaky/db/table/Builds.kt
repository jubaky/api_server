package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.joda.time.DateTime

object Builds : IntIdTable("build") {
    val branch = varchar("branch", 128)
    val tag = varchar("tag", 32)
    val result = text("result").nullable()
    val creator = reference(
        "creator_id",
        Users,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.CASCADE
    )
    val createTime = datetime("create_time").default(DateTime.now())
    val startTime = datetime("start_time").nullable()
    val finishTime = datetime("finish_time").nullable()
}
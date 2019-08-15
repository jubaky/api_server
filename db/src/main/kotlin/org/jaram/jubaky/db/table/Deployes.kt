package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.joda.time.DateTime

object Deployes : IntIdTable("deploy") {
    val build = reference("build_id", Builds, onDelete = ReferenceOption.NO_ACTION, onUpdate = ReferenceOption.CASCADE)
    val template = reference("template_id", Templates)
    val creator = reference(
        "creator_id",
        Users,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.CASCADE
    )
    val createTime = datetime("create_time").default(DateTime.now())
    val finishTime = datetime("finish_time").nullable()
}
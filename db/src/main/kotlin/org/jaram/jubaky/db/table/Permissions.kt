package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Permissions : IntIdTable("Permissions") {
    val groupId = reference("group_id", Groups, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val application = reference(
        "application_id",
        Applications,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val permission = varchar("permission", 3)
}
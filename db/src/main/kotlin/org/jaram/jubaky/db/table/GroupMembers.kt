package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable

object GroupMembers : IntIdTable("GroupMembers") {
    val groupId = reference("group_id", Groups)
    val userId = reference("user_id", Users)
}
package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable

object GroupMembers : IntIdTable("group_member") {
    val group = reference("group_id", Groups)
    val user = reference("user_id", Users)
}
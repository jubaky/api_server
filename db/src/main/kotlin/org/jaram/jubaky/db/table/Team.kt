package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Team : IntIdTable("team") {
    val name: Column<String> = varchar("name", 256).uniqueIndex()
    val teamLeader: Column<String> = varchar("team_leader", 256)
}
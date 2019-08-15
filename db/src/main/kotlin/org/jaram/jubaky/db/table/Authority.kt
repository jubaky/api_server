package org.jaram.jubaky.db.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Authority : Table() {
    val teamId: Column<Int> = integer("team_id").references(Team.id).primaryKey()
    val projectId: Column<Int> = integer("project_id").references(Project.id).primaryKey()
}
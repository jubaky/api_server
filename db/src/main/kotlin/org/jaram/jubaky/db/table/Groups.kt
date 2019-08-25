package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable

object Groups : IntIdTable("Groups") {
    val name = varchar("name", 256).uniqueIndex()
}
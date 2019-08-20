package org.jaram.jubaky.db.table

import org.jetbrains.exposed.dao.IntIdTable

object Jobs : IntIdTable("Jobs") {
    val branch = Builds.varchar("branch", 128)
    val applicationId = Builds.reference("application_id", Applications)
    val lastBuildNumber = integer("last_build_number").default(0).nullable()
    val tag = varchar("tag", 128).default("lts").nullable()
}
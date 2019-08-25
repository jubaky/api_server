package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Applications
import org.jaram.jubaky.db.table.Jobs
import org.jaram.jubaky.protocol.JobInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*

class JobDao(private val db: DB) {

    suspend fun getJobInfo(applicationId: Int, branch: String): JobInfo = db.read {
        Jobs.innerJoin(Applications).select {
            (Jobs.branch.eq(branch)) and (Jobs.applicationId.eq(applicationId))
        }.map {
            JobInfo(
                id = it[Jobs.id].value,
                branch = it[Jobs.branch],
                applicationName = it[Applications.name],
                lastBuildNumber = it[Jobs.lastBuildNumber],
                tag = it[Jobs.tag]
            )
        }.first()
    }

    suspend fun createJob(branch: String, applicationId: Int, tag: String) {
        db.execute {
            Jobs.insert {
                it[this.branch] = branch
                it[this.applicationId] = EntityID(applicationId, Applications)
                it[this.lastBuildNumber] = 0
                it[this.tag] = tag
            }
        }
    }

    suspend fun updateJob(applicationId: Int, branch: String, lastBuildNumber: Int) {
        db.execute {
            Jobs.update(
                where = { Jobs.applicationId.eq(applicationId).and(Jobs.branch.eq(branch)) },
                body = {
                    it[this.lastBuildNumber] = lastBuildNumber
                }
            )
        }
    }
}
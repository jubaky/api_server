package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.*
import org.jaram.jubaky.enumuration.BuildStatus
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.protocol.BuildInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

class BuildDao(private val db: DB) {
    suspend fun createBuilds(branch: String, jobId: Int, tag: String, result: String?, status: String, applicationId: Int, creatorId: Int, createTime: DateTime) {
        db.execute {
            Builds.insert {
                it[this.branch] = branch
                it[this.jobId] = EntityID(jobId, Jobs)
                it[this.tag] = tag
                it[this.result] = result
                it[this.status] = status
                it[this.application] = EntityID(applicationId, Applications)
                it[this.creator] = EntityID(creatorId, Users)
                it[this.createTime] = createTime
            }
        }
    }

    suspend fun getRecentBuildList(applicationId: Int, userGroupId: Int, count: Int, branch: String?): List<BuildInfo> {
        if (branch == null)
            return db.read {
                Builds.innerJoin(Users).innerJoin(Applications).innerJoin(Permissions).select {
                    Builds.application.eq(applicationId) and Permissions.application.eq(applicationId) and Permissions.groupId.eq(userGroupId)
                }.orderBy(Builds.createTime to SortOrder.DESC).limit(count).map {
                    BuildInfo (
                        id = it[Builds.id].value,
                        branch = it[Builds.branch],
                        jobId = it[Builds.jobId].value,
                        tag = it[Builds.tag],
                        buildNumber = 0, /** Need to put correct value **/
                        creatorName = it[Users.name],
                        createTime = it[Builds.createTime],
                        startTime = it[Builds.startTime],
                        endTime = it[Builds.finishTime],
                        applicationName = it[Applications.name],
                        /** Need to put correct values **/
                        status = toBuildStatus(it[Builds.status]),
                        progressRate = 100.0,
                        recentHistory = listOf(BuildInfo.BuildHistoryItem (
                            duration = 1,
                            isSuccess = true
                        ))
                    )
                }
            }

        return db.read {
            Builds.innerJoin(Users).innerJoin(Applications).select {
                Builds.application.eq(applicationId) and Builds.branch.eq(branch) and Permissions.application.eq(applicationId) and Permissions.groupId.eq(userGroupId)
            }.orderBy(Builds.createTime to SortOrder.DESC).limit(count).map {
                BuildInfo (
                    id = it[Builds.id].value,
                    branch = it[Builds.branch],
                    jobId = it[Builds.jobId].value,
                    tag = it[Builds.tag],
                    buildNumber = 0, /** Need to put correct value **/
                    creatorName = it[Users.name],
                    createTime = it[Builds.createTime],
                    startTime = it[Builds.startTime],
                    endTime = it[Builds.finishTime],
                    applicationName = it[Applications.name],
                    /** Need to put correct values **/
                    status = toBuildStatus(it[Builds.status]),
                    progressRate = 100.0,
                    recentHistory = listOf(
                        BuildInfo.BuildHistoryItem(
                            duration = 1,
                            isSuccess = true
                        )
                    )
                )
            }
        }
    }

    suspend fun getBuildInfo(buildId: Int): BuildInfo = db.read {
        Builds.innerJoin(Users).innerJoin(Applications).select {
            Builds.id.eq(buildId)
        }.map {
            BuildInfo (
                id = it[Builds.id].value,
                branch = it[Builds.branch],
                jobId = it[Builds.jobId].value,
                tag = it[Builds.tag],
                buildNumber = 0, /** Need to put correct value **/
                creatorName = it[Users.name],
                createTime = it[Builds.createTime],
                startTime = it[Builds.startTime],
                endTime = it[Builds.finishTime],
                applicationName = it[Applications.name],
                /** Need to put correct values **/
                status = toBuildStatus(it[Builds.status]),
                progressRate = 100.0,
                recentHistory = listOf(
                    BuildInfo.BuildHistoryItem(
                        duration = 1,
                        isSuccess = true
                    )
                )
            )
        }.first()
    }

    suspend fun getBuildInfo(applicationId: Int, branch: String): BuildInfo = db.read {
        Builds.innerJoin(Users).innerJoin(Applications).select {
            Builds.application.eq(applicationId) and Builds.branch.eq(branch)
        }.map {
            BuildInfo (
                id = it[Builds.id].value,
                branch = it[Builds.branch],
                jobId = it[Builds.jobId].value,
                tag = it[Builds.tag],
                buildNumber = 0, /** Need to put correct value **/
                creatorName = it[Users.name],
                createTime = it[Builds.createTime],
                startTime = it[Builds.startTime],
                endTime = it[Builds.finishTime],
                applicationName = it[Applications.name],
                /** Need to put correct values **/
                status = toBuildStatus(it[Builds.status]),
                progressRate = 100.0,
                recentHistory = listOf(
                    BuildInfo.BuildHistoryItem(
                        duration = 1,
                        isSuccess = true
                    )
                )
            )
        }.first()
    }

    suspend fun getCreatorId(buildId: Int): Int = db.read {
        Builds.select { Builds.id.eq(buildId) }.map {
            it[Builds.id].value
        }.first()
    }

    suspend fun updateBuildStatus(buildId: Int, status: String, startTime: DateTime, endTime: DateTime) {
        db.execute {
            Builds.update(
                where = { Builds.id.eq(buildId) },
                body = {
                    it[this.status] = status
                    it[this.startTime] = startTime
                    it[this.finishTime] = endTime
                }
            )
        }
    }
}
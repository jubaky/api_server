package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.BuildInfo
import org.joda.time.DateTime

interface BuildRepository {
    suspend fun createBuilds(branch: String, jobId: Int, tag: String, result: String?, status: String, applicationId: Int, creatorId: Int, createTime: DateTime)

    suspend fun getRecentBuildList(applicationId: Int, count: Int, branch: String?): List<BuildInfo>

    suspend fun getBuildInfo(buildId: Int): BuildInfo

    suspend fun getBuildInfo(applicationId: Int, branch: String): BuildInfo

    suspend fun getCreatorId(buildId: Int): Int

    suspend fun updateBuildStatus(buildId: Int, status: String, startTime: DateTime, endTime: DateTime)
}
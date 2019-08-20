package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.BuildDao
import org.jaram.jubaky.protocol.BuildInfo
import org.jaram.jubaky.repository.BuildRepository
import org.joda.time.DateTime

class BuildRepositoryImpl(
    private val buildDao: BuildDao
) : BuildRepository {
    override suspend fun createBuilds(branch: String, jobId: Int, tag: String, result: String?, status: String, applicationId: Int, creatorId: Int, createTime: DateTime) {
        buildDao.createBuilds(branch, jobId, tag, result, status, applicationId, creatorId, createTime)
    }

    override suspend fun getRecentBuildList(applicationId: Int, count: Int, branch: String?): List<BuildInfo> {
        return buildDao.getRecentBuildList(applicationId, count, branch)
    }

    override suspend fun getBuildInfo(buildId: Int): BuildInfo {
        return buildDao.getBuildInfo(buildId)
    }

    override suspend fun getBuildInfo(applicationId: Int, branch: String): BuildInfo {
        return buildDao.getBuildInfo(applicationId, branch)
    }

    override suspend fun getCreatorId(buildId: Int): Int {
        return buildDao.getCreatorId(buildId)
    }

    override suspend fun updateBuildStatus(buildId: Int, status: String, startTime: DateTime, endTime: DateTime) {
        buildDao.updateBuildStatus(buildId, status, startTime, endTime)
    }
}
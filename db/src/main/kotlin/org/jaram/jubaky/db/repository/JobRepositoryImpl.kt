package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.JobDao
import org.jaram.jubaky.protocol.JobInfo
import org.jaram.jubaky.repository.JobRepository

class JobRepositoryImpl(
    private val jobDao: JobDao
) : JobRepository {

    override suspend fun getJobInfo(applicationId: Int, branch: String): JobInfo {
        println("$applicationId, $branch")
        return jobDao.getJobInfo(applicationId, branch)
    }

    override suspend fun createJob(branch: String, applicationId: Int, tag: String) {
        jobDao.createJob(branch, applicationId, tag)
    }

    override suspend fun updateJob(applicationId: Int, branch: String, lastBuildNumber: Int) {
        jobDao.updateJob(applicationId, branch, lastBuildNumber)
    }
}
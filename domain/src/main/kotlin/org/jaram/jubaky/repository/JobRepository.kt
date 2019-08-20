package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.JobInfo

interface JobRepository {

    suspend fun getJobInfo(applicationId: Int, branch: String): JobInfo

    suspend fun createJob(branch: String, applicationId: Int, tag: String)

    suspend fun updateJob(applicationId: Int, branch: String, lastBuildNumber: Int)
}
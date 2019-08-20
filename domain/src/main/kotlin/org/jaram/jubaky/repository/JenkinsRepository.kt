package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.Application
import org.jaram.jubaky.domain.jenkins.*
import org.jaram.jubaky.protocol.BuildInfo
import org.jaram.jubaky.protocol.JobInfo

interface JenkinsRepository {

    suspend fun startPipeline(pipeline: Pipeline)

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String)

    suspend fun getJob(applicationId: Int, branchName: String): Job

    suspend fun getJobSpec(applicationId: Int, branchName: String, buildNumber: Int): JobSpec

    suspend fun getJobLog(buildInfo: BuildInfo): JobLog

    suspend fun createJob(applicationId: Int, userId: Int, applicationData: Application, configData: JobConfig)

    suspend fun deleteJob(jobName: String, branchName: String)

    suspend fun updateJob(jobName: String, configData: JobConfig)

    suspend fun buildWithParameters(applicationId: Int, jobInfo: JobInfo, buildArgumentList: List<BuildArgument>)

    suspend fun getPendingBuildList(): List<String>

    suspend fun createCredentials(credentials: Credentials)

    suspend fun deleteCredentials(key: String)

    fun replaceNameWithBranch(prefix: String, branchName: String): String
}
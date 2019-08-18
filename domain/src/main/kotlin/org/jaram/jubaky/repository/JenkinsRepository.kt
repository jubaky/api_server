package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.jenkins.Pipeline
import org.jaram.jubaky.domain.jenkins.*

interface JenkinsRepository {

    suspend fun startPipeline(pipeline: Pipeline)

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String)

    suspend fun getJob(jobName: String, branchName: String): Job

    suspend fun getJobSpec(jobName: String, branchName: String, buildNumber: Int): JobSpec

    suspend fun getJobLog(jobName: String, branchName: String, buildNumber: Int): JobLog

    suspend fun createJob(jobName: String, configData: JobConfig)

    suspend fun deleteJob(jobName: String, branchName: String)

    suspend fun updateJob(jobName: String, configData: JobConfig)

    suspend fun buildWithParameters(jobName: String, branchName: String, buildArgumentList: List<BuildArgument>)

    suspend fun getPendingBuildList(): List<String>

    suspend fun createCredentials(credentials: Credentials)

    suspend fun deleteCredentials(key: String)

    fun replaceNameWithBranch(prefix: String, branchName: String): String
}
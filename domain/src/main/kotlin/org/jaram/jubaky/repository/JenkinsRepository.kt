package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.jenkins.Pipeline
import org.jaram.jubaky.domain.jenkins.*

interface JenkinsRepository {

    suspend fun startPipeline(pipeline: Pipeline)

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String)

    suspend fun getJob(jobName: String): Job?

    suspend fun getJobSpec(jobName: String, buildNumber: String): JobSpec?

    suspend fun getJobLog(jobName: String, buildNumber: String): JobLog

    suspend fun createJob(jobName: String, configData: JobConfig)

    suspend fun deleteJob(jobName: String)

    suspend fun updateJob(jobName: String, configData: JobConfig)

    suspend fun buildWithParameters(jobName: String, buildArgumentList: List<BuildArgument>)

    suspend fun createCredentials(credentials: Credentials)

    suspend fun deleteCredentials(key: String)
}
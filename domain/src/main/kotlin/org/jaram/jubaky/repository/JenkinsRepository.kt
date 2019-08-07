package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.Pipeline
import org.jaram.jubaky.domain.jenkins.*

interface JenkinsRepository {

    suspend fun startPipeline(pipeline: Pipeline)

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String)

    suspend fun getJob(jobName: String): Job?

    suspend fun getJobSpec(jobName: String, buildNumber: String): JobSpec?

    suspend fun getJobLog(jobName: String, buildNumber: String): String?

    suspend fun createJob(jobName: String, configData: JobConfig)

    suspend fun deleteJob(jobName: String)

    suspend fun updateJob(jobName: String, configData: JobConfig)

    suspend fun buildWithParameters(jobName: String, buildArgumentList: List<BuildArgument>)

    suspend fun createCredentials(username: String, password: String, key: String, description: String, scope: String)

    suspend fun deleteCredentials(key: String)
}
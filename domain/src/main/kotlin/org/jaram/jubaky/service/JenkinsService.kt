package org.jaram.jubaky.service

import org.jaram.jubaky.domain.jenkins.*
import org.jaram.jubaky.repository.JenkinsRepository


class JenkinsService(
    private val jenkinsRepository: JenkinsRepository
) {

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String) {
        jenkinsRepository.redirectGithubWebhook(headers, payload)
    }

    suspend fun getJob(jobName: String): Job? {
        return jenkinsRepository.getJob(jobName)
    }

    suspend fun getJobSpec(jobName: String, buildNumber: String): JobSpec? {
        return jenkinsRepository.getJobSpec(jobName, buildNumber)
    }

    suspend fun getJobLog(jobName: String, buildNumber: String): JobLog {
        return jenkinsRepository.getJobLog(jobName, buildNumber)
    }

    suspend fun buildWithParameters(jobName: String, buildArgumentList: List<BuildArgument>) {
        jenkinsRepository.buildWithParameters(jobName, buildArgumentList)
    }

    suspend fun createJob(jobName: String, configData: JobConfig) {
        jenkinsRepository.createJob(jobName, configData)
    }

    suspend fun deleteJob(jobName: String) {
        jenkinsRepository.deleteJob(jobName)
    }

    suspend fun updateJob(jobName: String, configData: JobConfig) {
        jenkinsRepository.updateJob(jobName, configData)
    }

    suspend fun createCredentials(credentials: Credentials) {
        jenkinsRepository.createCredentials(credentials)
    }

    suspend fun deleteCredentials(key: String) {
        jenkinsRepository.deleteCredentials(key)
    }
}
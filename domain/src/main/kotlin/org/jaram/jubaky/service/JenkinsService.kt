package org.jaram.jubaky.service

import org.jaram.jubaky.domain.jenkins.*
import org.jaram.jubaky.repository.JenkinsRepository


class JenkinsService(
    private val jenkinsRepository: JenkinsRepository
) {

    suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String) {
        jenkinsRepository.redirectGithubWebhook(headers, payload)
    }

    suspend fun getJob(jobName: String, branchName: String): Job {
        return jenkinsRepository.getJob(jobName, branchName)
    }

    suspend fun getJobSpec(jobName: String, branchName: String, buildNumber: Int): JobSpec {
        return jenkinsRepository.getJobSpec(jobName, branchName, buildNumber)
    }

    suspend fun getJobLog(jobName: String, branchName: String, buildNumber: Int): JobLog {
        return jenkinsRepository.getJobLog(jobName, branchName, buildNumber)
    }

    suspend fun buildWithParameters(jobName: String, branchName: String, buildArgumentList: List<BuildArgument>) {
        jenkinsRepository.buildWithParameters(jobName, branchName, buildArgumentList)
    }

    suspend fun createJob(jobName: String, configData: JobConfig) {
        jenkinsRepository.createJob(jobName, configData)
    }

    suspend fun deleteJob(jobName: String, branchName: String) {
        jenkinsRepository.deleteJob(jobName, branchName)
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
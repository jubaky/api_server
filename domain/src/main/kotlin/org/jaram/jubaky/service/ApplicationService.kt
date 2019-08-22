package org.jaram.jubaky.service

import org.jaram.jubaky.domain.jenkins.Credentials
import org.jaram.jubaky.protocol.CredentialInfo
import org.jaram.jubaky.repository.ApplicationRepository
import org.jaram.jubaky.repository.CredentialRepository
import org.jaram.jubaky.repository.GitRepository
import org.jaram.jubaky.repository.JenkinsRepository

class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val jenkinsRepository: JenkinsRepository,
    private val gitRepository: GitRepository,
    private val credentialRepository: CredentialRepository
) {

    suspend fun getApplicationList() = applicationRepository.getApplicationList()

    suspend fun getApplicationInfo(applicationId: Int) = applicationRepository.getApplicationInfo(applicationId)

    suspend fun getBranchList(applicationId: Int): List<String> {
        val gitRepositoryUrl = applicationRepository.getGitRepositoryUrl(applicationId)

        return gitRepository.getBranchList(gitRepositoryUrl)
    }

    suspend fun getCredentialList(userId: Int): List<CredentialInfo> {
        return credentialRepository.getCredentialList(userId)
    }

    suspend fun createCredentials(credentials: Credentials) {
        jenkinsRepository.createCredentials(credentials)

        return credentialRepository.createCredential(
            userName = credentials.username,
            password = credentials.password,
            key = credentials.key
        )
    }

    suspend fun deleteCredentials(key: String) {
        jenkinsRepository.deleteCredentials(key)

        return credentialRepository.deleteCredential(key)
    }
}
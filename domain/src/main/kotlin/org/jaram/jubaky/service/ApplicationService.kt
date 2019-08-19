package org.jaram.jubaky.service

import org.jaram.jubaky.repository.ApplicationRepository
import org.jaram.jubaky.repository.GitRepository

class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val gitRepository: GitRepository
) {

    suspend fun getApplicationList() = applicationRepository.getApplicationList()

    suspend fun getApplicationInfo(applicationId: Int) = applicationRepository.getApplicationInfo(applicationId)

    suspend fun getBranchList(applicationId: Int): List<String> {
        val gitRepositoryUrl = applicationRepository.getGitRepositoryUrl(applicationId)

        return gitRepository.getBranchList(gitRepositoryUrl)
    }
}
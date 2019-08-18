package org.jaram.jubaky.service

import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.protocol.BuildInfo
import org.jaram.jubaky.repository.BuildRepository
import org.jaram.jubaky.repository.JenkinsRepository

class BuildService(
    private val buildRepository: BuildRepository,
    private val jenkinsRepository: JenkinsRepository
) {

    fun getRecentBuildList(applicationId: Int, count: Int, branch: String? = null): List<BuildInfo> {
        return emptyList()
    }

    suspend fun getBuildLog(buildId: Int): String {
        val buildInfo: BuildInfo = buildRepository.getBuildInfo(buildId)

        return jenkinsRepository.getJobLog(buildInfo.applicationName, buildInfo.branch, buildInfo.buildNumber).log
    }

    suspend fun runBuild(applicationId: Int, branch: String, buildArgumentList: List<BuildArgument>) {
        val buildInfo: BuildInfo = buildRepository.getBuildInfo(applicationId)

        jenkinsRepository.buildWithParameters(buildInfo.applicationName, branch, buildArgumentList)
    }
}
package org.jaram.jubaky.service

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

    fun getBuildLog(buildId: Int): String {
        return ""
    }

    fun runBuild(applicationId: Int, branch: String) {

    }
}
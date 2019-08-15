package org.jaram.jubaky.service

import org.jaram.jubaky.repository.BuildRepository
import org.jaram.jubaky.repository.JenkinsRepository

class BuildService(
    private val buildRepository: BuildRepository,
    private val jenkinsRepository: JenkinsRepository
) {

    fun getRecentBuildList(applicationId: Int, count: Int, branch: String? = null) {

    }

    fun getBuildInfo(buildId: Int) {

    }

    fun runBuild(applicationId: Int, branch: String) {

    }
}
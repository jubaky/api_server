package org.jaram.jubaky.service

import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.JobConfig
import org.jaram.jubaky.protocol.BuildInfo
import org.jaram.jubaky.protocol.JobInfo
import org.jaram.jubaky.repository.ApplicationRepository
import org.jaram.jubaky.repository.BuildRepository
import org.jaram.jubaky.repository.JenkinsRepository
import org.jaram.jubaky.repository.JobRepository

class BuildService(
    private val buildRepository: BuildRepository,
    private val jenkinsRepository: JenkinsRepository,
    private val applicationRepository: ApplicationRepository,
    private val jobRepository: JobRepository
) {

    suspend fun getRecentBuildList(applicationId: Int, count: Int, branch: String? = null): List<BuildInfo> {
        return buildRepository.getRecentBuildList(applicationId, count, branch)
    }

    suspend fun getBuildInfo(buildId: Int): BuildInfo {
        return buildRepository.getBuildInfo(buildId)
    }

    suspend fun getBuildLog(buildId: Int): String {
        val buildInfo: BuildInfo = buildRepository.getBuildInfo(buildId)

        return jenkinsRepository.getJobLog(buildInfo).log
    }

    suspend fun createJob(applicationId: Int, configData: JobConfig) {
        val applicationInfo = applicationRepository.getApplicationInfo(applicationId)
        val userId = applicationRepository.getUserId(applicationInfo.id)

        jenkinsRepository.createJob(applicationId, userId, applicationInfo, configData)
    }

    suspend fun runBuild(applicationId: Int, branch: String, buildArgumentList: List<BuildArgument>) {
        val jobInfo: JobInfo = jobRepository.getJobInfo(applicationId, branch)

        jenkinsRepository.buildWithParameters(applicationId, jobInfo, buildArgumentList)
    }
}
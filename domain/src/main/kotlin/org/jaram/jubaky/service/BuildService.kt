package org.jaram.jubaky.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.Job
import org.jaram.jubaky.domain.jenkins.JobSpec
import org.jaram.jubaky.enumuration.BuildStatus
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.protocol.BuildInfo
import org.jaram.jubaky.repository.BuildRepository
import org.jaram.jubaky.repository.JenkinsRepository
import java.util.concurrent.TimeUnit

class BuildService(
    private val buildRepository: BuildRepository,
    private val jenkinsRepository: JenkinsRepository,
    private val startDelayTime: Int,
    private val intervalDelayTime: Int
) {

    fun getRecentBuildList(applicationId: Int, count: Int, branch: String? = null): List<BuildInfo> {
        return emptyList()
    }

    suspend fun getBuildLog(buildId: Int): String {
        val buildInfo: BuildInfo = buildRepository.getBuildInfo(buildId)

        return jenkinsRepository.getJobLog(buildInfo.applicationName, buildInfo.branch, buildInfo.buildNumber).log
    }

    suspend fun runBuild(applicationId: Int, branch: String, buildArgumentList: List<BuildArgument>): BuildStatus {
        val buildInfo: BuildInfo = buildRepository.getBuildInfo(applicationId)
        val jobInfo: Job = jenkinsRepository.getJob(buildInfo.applicationName, branch)

        jenkinsRepository.buildWithParameters(buildInfo.applicationName, branch, buildArgumentList)

        val resultJobSpec = getBuildResult(jenkinsRepository, jobInfo, branch)

        /**
         * Save data to DB
         */
//        val tag = buildInfo.tag
//        val status = toBuildStatus(resultJobSpec.result)
//        val createTime = resultJobSpec.createTimestamp
//        val startTime = resultJobSpec.createTimestamp + resultJobSpec.inQueueDuration
//        val finishTime = startTime + resultJobSpec.buildDuration

        return toBuildStatus(resultJobSpec.result)
    }

    private suspend fun getBuildResult(jenkinsRepository: JenkinsRepository, jobInfo: Job, branchName: String): JobSpec {
        val jobName = jobInfo.name
        val lastBuildNumber = jobInfo.lastBuildNumber
        val currentBuildNumber = lastBuildNumber + 1

//        withContext(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
        withContext(Dispatchers.IO) {
            checkBuildStatus(jobName, branchName, currentBuildNumber)

        }

        return jenkinsRepository.getJobSpec(jobName, branchName, currentBuildNumber)
    }

    private suspend fun checkBuildStatus(jobName: String, branchName: String, buildNumber: Int) {
        TimeUnit.MILLISECONDS.sleep(startDelayTime.toLong())

        /**
         * @TODO
         * 시간이 일정 boundary 를 넘으면 자동으로 종료하게 하기 (Timeout)
         */
        while (true) {
            val jobSpec = jenkinsRepository.getJobSpec(jobName, branchName, buildNumber)

            if (jobSpec.result != null)
                break

            TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())
        }
    }
}
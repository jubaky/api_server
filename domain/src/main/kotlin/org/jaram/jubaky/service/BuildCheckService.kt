package org.jaram.jubaky.service

import com.google.common.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jaram.jubaky.JenkinsBuildDuplicationException
import org.jaram.jubaky.domain.checker.Build
import org.jaram.jubaky.domain.checker.toBuild
import org.jaram.jubaky.enumuration.BuildStatus
import org.jaram.jubaky.enumuration.buildStatusToString
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.repository.ApplicationRepository
import org.jaram.jubaky.repository.BuildRepository
import org.jaram.jubaky.repository.JenkinsRepository
import org.joda.time.DateTime
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

class BuildCheckService(
    private val buildRepository: BuildRepository,
    private val applicationRepository: ApplicationRepository,
    private val intervalDelayTime: Int,
    private val intervalCheckHealthTime: Int
) {

    lateinit var jenkinsRepository: JenkinsRepository

    val buildEventBus = EventBus("BuildEventBus")

    private val abortedBuildList = ArrayList<Build>()
    private val pendingBuildList = ArrayList<Build>()
    private val progressBuildList = ArrayList<Build>()
    private val successBuildList = ArrayList<Build>()
    private val failureBuildList = ArrayList<Build>()

    private lateinit var buildCheckJob: Job
    private lateinit var checkHealthJob: Job

    fun runBuildCheck() {
        buildCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                // Check the pending build list
                val pendingBuildIdxList = mutableListOf<Int>()
                val pendingBuildListInJenkins = jenkinsRepository.getPendingBuildList()

                pendingBuildList.map { pendingBuild ->
                    val branchedJobName = jenkinsRepository.replaceNameWithBranch(pendingBuild.applicationName, pendingBuild.branch)

                    if (!pendingBuildListInJenkins.contains(branchedJobName))  // If PENDING -> PROGRESS
                        pendingBuildIdxList.add(pendingBuildList.indexOf(pendingBuild))
                }

                pendingBuildIdxList.map { idx ->
                    val progressBuild = pendingBuildList.removeAt(idx)

                    progressBuildList.add(progressBuild)
                }

                TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())

                // Check build queue lists
                val abortedBuildIdxList = mutableListOf<Int>()
                val successBuildIdxList = mutableListOf<Int>()
                val failureBuildIdxList = mutableListOf<Int>()

                for (i in 0 until progressBuildList.size) {
                    val build: Build = progressBuildList[i]
                    val applicationInfo = applicationRepository.getApplicationInfo(build.applicationName)

                    val job = jenkinsRepository.getJob(applicationInfo.id, build.branch)
                    val jobSpec = jenkinsRepository.getJobSpec(applicationInfo.id, build.branch, build.buildNumber)

                    when (toBuildStatus(jobSpec.result)) {
                        BuildStatus.ABORTED -> abortedBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.SUCCESS -> successBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.FAILURE -> failureBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.PENDING -> {}
                        BuildStatus.PROGRESS -> {}
                        BuildStatus.UNKNOWN -> {}  // Throw an Exception ?
                    }

                    progressBuildList[i] = toBuild(build.buildId, job, jobSpec)
                }

                abortedBuildIdxList.map { idx -> abortedBuildList.add(progressBuildList.removeAt(idx)) }
                successBuildIdxList.map { idx -> successBuildList.add(progressBuildList.removeAt(idx)) }
                failureBuildIdxList.map { idx -> failureBuildList.add(progressBuildList.removeAt(idx)) }

                // Update data to DB
                abortedBuildList.map { build -> updateDatabase(build) } // Update Status
                TimeUnit.MILLISECONDS.sleep(200)
                pendingBuildList.map { build -> updateDatabase(build) }
                TimeUnit.MILLISECONDS.sleep(200)
                progressBuildList.map { build -> updateDatabase(build) }
                TimeUnit.MILLISECONDS.sleep(200)
                successBuildList.map { build -> updateDatabase(build) }
                TimeUnit.MILLISECONDS.sleep(200)
                failureBuildList.map { build -> updateDatabase(build) }
                TimeUnit.MILLISECONDS.sleep(200)

                /**
                 * @TODO
                 * Deep clone and doing post
                 */
                buildEventBus.post(
                    mapOf(
                        "abortedBuildList" to abortedBuildList,
                        "pendingBuildList" to pendingBuildList,
                        "progressBuildList" to progressBuildList,
                        "successBuildList" to successBuildList,
                        "failureBuildList" to failureBuildList
                    )
                )

//                println(mapOf(
//                    "abortedBuildList" to abortedBuildList,
//                    "pendingBuildList" to pendingBuildList,
//                    "progressBuildList" to progressBuildList,
//                    "successBuildList" to successBuildList,
//                    "failureBuildList" to failureBuildList
//                ))
//                println(jenkinsRepository.getPendingBuildList())

                abortedBuildList.clear()
                successBuildList.clear()
                failureBuildList.clear()

                TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())
            }
        }
    }

    fun stopBuildCheck() {
        buildCheckJob.cancel()
    }

    fun runCheckHealth() {
        checkHealthJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (!buildCheckJob.isActive) {
                    runBuildCheck()
                }

                TimeUnit.MILLISECONDS.sleep(intervalCheckHealthTime.toLong())
            }
        }
    }

    fun stopCheckHealth() {
        checkHealthJob.cancel()
    }

    fun getPendingBuildList(): ArrayList<Build> {
        return pendingBuildList
    }

    fun isBuildCheckJobRunning(): Boolean {
        return buildCheckJob.isActive
    }

    private suspend fun updateDatabase(build: Build) {
        buildRepository.updateBuildStatus(
            build.buildId,
            buildStatusToString(build.status),
            DateTime(build.startTime),
            DateTime(build.endTime)
        )
    }

    suspend fun checkBuildDuplication(jobName: String, branchName: String) {
        val branchedJobName = jenkinsRepository.replaceNameWithBranch(jobName, branchName)

        // Check PENDING
        jenkinsRepository.getPendingBuildList().map { pendingBuildName ->
            if (pendingBuildName == branchedJobName)
                throw JenkinsBuildDuplicationException()
        }
    }
}
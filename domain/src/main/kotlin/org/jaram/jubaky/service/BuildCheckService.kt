package org.jaram.jubaky.service

import com.google.common.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jaram.jubaky.JenkinsBuildDuplicationException
import org.jaram.jubaky.domain.checker.Build
import org.jaram.jubaky.enumuration.BuildStatus
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.repository.JenkinsRepository
import java.util.concurrent.TimeUnit

class BuildCheckService(
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
    private lateinit var pendingBuildCheckJob: Job
    private lateinit var checkHealthJob: Job

    fun runBuildCheck() {
        buildCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val abortedBuildIdxList = mutableListOf<Int>()
                val successBuildIdxList = mutableListOf<Int>()
                val failureBuildIdxList = mutableListOf<Int>()

                for (i in 0 until progressBuildList.size) {
                    val build = progressBuildList[i]
                    val jobSpec = jenkinsRepository.getJobSpec(build.name, build.branch, build.buildNumber)
                    val buildStatus = toBuildStatus(jobSpec.result ?: "")

                    when (buildStatus) {
                        BuildStatus.ABORTED -> abortedBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.SUCCESS -> successBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.FAILURE -> failureBuildIdxList.add(progressBuildList.indexOf(build))
                        BuildStatus.PENDING -> {}
                        BuildStatus.PROGRESS -> {}
                        BuildStatus.UNKNOWN -> {}  // Throw an Exception ?
                    }

                    progressBuildList[i] = Build(
                        name = build.name,
                        branch = build.branch,
                        buildNumber = build.buildNumber,
                        status = buildStatus
                    )
                }

                abortedBuildIdxList.map { idx -> abortedBuildList.add(progressBuildList.removeAt(idx)) }
                successBuildIdxList.map { idx -> successBuildList.add(progressBuildList.removeAt(idx)) }
                failureBuildIdxList.map { idx -> failureBuildList.add(progressBuildList.removeAt(idx)) }

                /**
                 * Save data to DB
                 */

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

    fun runPendingBuildCheck() {
        pendingBuildCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val pendingBuildIdxList = mutableListOf<Int>()

                val pendingBuildListInJenkins = jenkinsRepository.getPendingBuildList()

                pendingBuildList.map { pendingBuild ->
                    val branchedJobName = jenkinsRepository.replaceNameWithBranch(pendingBuild.name, pendingBuild.branch)

                    if (!pendingBuildListInJenkins.contains(branchedJobName))  // If PENDING -> PROGRESS
                        pendingBuildIdxList.add(pendingBuildList.indexOf(pendingBuild))
                }

                pendingBuildIdxList.map { idx -> progressBuildList.add(pendingBuildList.removeAt(idx)) }


                TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())
            }
        }
    }

    fun stopPendingBuildCheck() {
        pendingBuildCheckJob.cancel()
    }

    fun runCheckHealth() {
        checkHealthJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (!buildCheckJob.isActive) {
                    runBuildCheck()
                } else if (!pendingBuildCheckJob.isActive) {
                    runPendingBuildCheck()
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

    fun isPendingBuildCheckJobRunning(): Boolean {
        return pendingBuildCheckJob.isActive
    }

    suspend fun checkBuildDuplication(jobName: String, branchName: String): Boolean {
        var isDuplicated = false
        var isPending = false
        var isProgress = false
        val branchedJobName = jenkinsRepository.replaceNameWithBranch(jobName, branchName)

        // Check PENDING
        jenkinsRepository.getPendingBuildList().map { pendingBuildName ->
            if (pendingBuildName == branchedJobName)
                isPending = true
        }

        // If build is in Jenkins build queue and is in Jubaky pending queue
        if (isPending) {
            pendingBuildList.map { pendingBuild ->
                val branchedPendingJobName =
                    jenkinsRepository.replaceNameWithBranch(pendingBuild.name, pendingBuild.branch)

                if (branchedPendingJobName == branchedJobName)
                    return true
            }
        }

        // Check PROGRESS
        progressBuildList.map { progressBuild ->
            val branchedProgressJobName =
                jenkinsRepository.replaceNameWithBranch(progressBuild.name, progressBuild.branch)

            if (branchedProgressJobName == branchedJobName)
                isProgress = true
        }

        isDuplicated = isPending && isProgress

        if (!isDuplicated)
            return isDuplicated
        else
            throw JenkinsBuildDuplicationException()
    }
}
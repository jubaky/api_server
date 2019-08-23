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
import org.jaram.jubaky.repository.JobRepository
import org.joda.time.DateTime
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

class BuildCheckService(
    private val buildRepository: BuildRepository,
    private val applicationRepository: ApplicationRepository,
    private val jobRepository: JobRepository,
    private val intervalDelayTime: Int,
    private val intervalCheckHealthTime: Int
) {

    lateinit var jenkinsRepository: JenkinsRepository

    val buildEventBus = EventBus("BuildEventBus")

    private val abortedBuildList = CopyOnWriteArrayList<Build>()
    private val pendingBuildList = CopyOnWriteArrayList<Build>()
    private val progressBuildList = CopyOnWriteArrayList<Build>()
    private val successBuildList = CopyOnWriteArrayList<Build>()
    private val failureBuildList = CopyOnWriteArrayList<Build>()

    private lateinit var buildCheckJob: Job
    private lateinit var checkHealthJob: Job

    fun runBuildCheck() {
        buildCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val pendingBuildListInJenkins = jenkinsRepository.getPendingBuildList()

                // If new build is in queue, insert to pending queue
                for (i in 0 until pendingBuildListInJenkins.size) {
                    val pendingBuildInJenkinsString = pendingBuildListInJenkins[i].split("0branch0")

                    val applicationName = pendingBuildInJenkinsString[0]
                    val branchName = pendingBuildInJenkinsString[1].replace("_", "/")

                    val applicationInfo = applicationRepository.getApplicationInfo(applicationName)
                    val jobInfo = jobRepository.getJobInfo(applicationInfo.id, branchName)

                    var checkIsNewPending = true
                    for (j in 0 until pendingBuildList.size) {
                        val pendingBuildBranchedName = jenkinsRepository.replaceNameWithBranch(
                            pendingBuildList[j].applicationName,
                            pendingBuildList[j].branch
                        )

                        if (pendingBuildBranchedName == pendingBuildListInJenkins[i]) {
                            checkIsNewPending = false
                        }
                    }

                    if (checkIsNewPending) {
                        buildRepository.createBuilds(
                            branch = branchName,
                            jobId = jobInfo.id,
                            tag = jobInfo.tag,
                            result = "",
                            status = buildStatusToString(BuildStatus.PENDING),
                            applicationId = applicationInfo.id,
                            creatorId = 1,
                            createTime = DateTime()
                        )

                        val buildInfo = buildRepository.getBuildInfo(applicationInfo.id, branchName)
                        val currentBuildNumber = jobInfo.lastBuildNumber + 1

                        pendingBuildList.add(
                            Build(
                                buildId = buildInfo.id,
                                jobId = buildInfo.jobId,
                                applicationName = applicationName,
                                branch = branchName,
                                buildNumber = currentBuildNumber,
                                status = toBuildStatus("PENDING"),
                                createTime = System.currentTimeMillis(),
                                startTime = 0,
                                endTime = 0,
                                progressRate = 100.0
                            )
                        )
                    }
                }

                // Check pending queue
                val pendingBuildRemovalIdxList = mutableListOf<Int>()
                val pendingBuildListTemp = mutableListOf<Build>()

                for (i in 0 until pendingBuildList.size) {
                    val pendingBuild = pendingBuildList[i]
                    val branchedJobName = jenkinsRepository.replaceNameWithBranch(pendingBuild.applicationName, pendingBuild.branch)

                    if (!pendingBuildListInJenkins.contains(branchedJobName)) {  // If PENDING -> PROGRESS
                        pendingBuildListTemp.add(
                            Build(
                                buildId = pendingBuild.buildId,
                                jobId = pendingBuild.jobId,
                                applicationName = pendingBuild.applicationName,
                                branch = pendingBuild.branch,
                                buildNumber = pendingBuild.buildNumber,
                                status = BuildStatus.PROGRESS,
                                createTime = pendingBuild.createTime,
                                startTime = System.currentTimeMillis(),
                                endTime = 0,
                                progressRate = 100.0
                            )
                        )

                        pendingBuildRemovalIdxList.add(i)
                    }
                }

                pendingBuildListTemp.forEach { build -> progressBuildList.add(build)}
                pendingBuildRemovalIdxList.forEach { idx -> pendingBuildList.removeAt(idx) }

                pendingBuildList.forEach { build -> updateBuildStatus(build) }
                TimeUnit.MILLISECONDS.sleep(200)

                TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())

                // Check build queue
                progressBuildList.forEach { build -> updateBuildStatus(build) }
                TimeUnit.MILLISECONDS.sleep(200)

                val progressBuildListTemp = mutableListOf<Build>()
                val progressBuildIdxList = mutableListOf<Int>()

                progressBuildList.forEach { build -> progressBuildListTemp.add(build) }

                for (i in 0 until progressBuildListTemp.size) {
                    val build = progressBuildListTemp[i]
                    val applicationInfo = applicationRepository.getApplicationInfo(build.applicationName)
                    val jobInfo = jobRepository.getJobInfo(applicationInfo.id, build.branch)
                    val jobSpec = jenkinsRepository.getJobSpec(applicationInfo.id, build.branch, build.buildNumber)

                    when (toBuildStatus(jobSpec.result)) {
                        BuildStatus.ABORTED -> {
                            abortedBuildList.add(toBuild(build.buildId, jobInfo, jobSpec))
                            progressBuildIdxList.add(i)
                        }
                        BuildStatus.SUCCESS -> {
                            successBuildList.add(toBuild(build.buildId, jobInfo, jobSpec))
                            progressBuildIdxList.add(i)
                        }
                        BuildStatus.FAILURE -> {
                            failureBuildList.add(toBuild(build.buildId, jobInfo, jobSpec))
                            progressBuildIdxList.add(i)
                        }
                        BuildStatus.PENDING -> {}
                        BuildStatus.PROGRESS -> {}
                        BuildStatus.UNKNOWN -> {}  // Throw an Exception ?
                    }
                }

                // Update data to DB
                abortedBuildList.forEach { build -> updateBuildStatus(build) } // Update Status
                TimeUnit.MILLISECONDS.sleep(200)
                successBuildList.forEach { build -> updateBuildStatus(build) }
                TimeUnit.MILLISECONDS.sleep(200)
                failureBuildList.forEach { build -> updateBuildStatus(build) }
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

                println(mapOf(
                    "abortedBuildList" to abortedBuildList,
                    "pendingBuildList" to pendingBuildList,
                    "progressBuildList" to progressBuildList,
                    "successBuildList" to successBuildList,
                    "failureBuildList" to failureBuildList
                ))
                println(jenkinsRepository.getPendingBuildList())

                abortedBuildList.clear()
                successBuildList.clear()
                failureBuildList.clear()
                progressBuildIdxList.forEach { idx -> progressBuildList.removeAt(idx) }

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

    fun getPendingBuildList(): CopyOnWriteArrayList<Build> {
        return pendingBuildList
    }

    fun isBuildCheckJobRunning(): Boolean {
        return buildCheckJob.isActive
    }

    private suspend fun updateBuildStatus(build: Build) {
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
        jenkinsRepository.getPendingBuildList().forEach { pendingBuildName ->
            if (pendingBuildName == branchedJobName)
                throw JenkinsBuildDuplicationException()
        }
    }
}
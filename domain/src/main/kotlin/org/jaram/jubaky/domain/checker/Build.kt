package org.jaram.jubaky.domain.checker

import org.jaram.jubaky.domain.jenkins.JobSpec
import org.jaram.jubaky.enumuration.BuildStatus
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.protocol.JobInfo

data class Build(
    val buildId: Int,
    val jobId: Int,
    val applicationName: String,
    val branch: String,
    val buildNumber: Int,
    val status: BuildStatus,
    val createTime: Long,
    val startTime: Long,
    val endTime: Long,
    val progressRate: Double = 100.0
)

fun toBuild(buildId: Int, job: JobInfo, jobSpec: JobSpec): Build {
    val createTimestamp = jobSpec.createTimestamp
    val startTimestamp = createTimestamp + jobSpec.inQueueDuration.toLong()
    val endTimestamp = startTimestamp + jobSpec.buildDuration.toLong()

    return Build(
        buildId = buildId,
        jobId = job.id,
        applicationName = job.applicationName,
        branch = job.branch,
        buildNumber = jobSpec.number ?: 1,
        createTime = createTimestamp,
        startTime = startTimestamp,
        endTime = endTimestamp,
        status = toBuildStatus(jobSpec.result?: "UNKNOWN"),
        progressRate = 100.toDouble()
    )
}
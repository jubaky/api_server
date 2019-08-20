package org.jaram.jubaky.protocol

import org.jaram.jubaky.enumuration.BuildStatus
import org.joda.time.DateTime

data class BuildInfo(
    val id: Int,
    val applicationName: String,
    val branch: String,
    val jobId: Int,
    val tag: String,
    val buildNumber: Int,
    val creatorName: String,
    val createTime: DateTime,
    val startTime: DateTime?,
    val endTime: DateTime?,
//    val currentStage: String,
    val status: BuildStatus,
    val progressRate: Double,
    val recentHistory: List<BuildHistoryItem>
) {
    data class BuildHistoryItem(
        val duration: Int,
        val isSuccess: Boolean
    )
}

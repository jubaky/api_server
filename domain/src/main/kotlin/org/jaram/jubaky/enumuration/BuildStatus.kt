package org.jaram.jubaky.enumuration

enum class BuildStatus {
    ABORTED,
    PENDING,
    PROGRESS,
    SUCCESS,
    FAILURE,
    UNKNOWN;
}

fun toBuildStatus(status: String?): BuildStatus {
    return if (status != null) {
        when (status.toUpperCase()) {
            "ABORTED" -> BuildStatus.ABORTED
            "PENDING" -> BuildStatus.PENDING
            "PROGRESS" -> BuildStatus.PROGRESS
            "SUCCESS" -> BuildStatus.SUCCESS
            "FAILURE" -> BuildStatus.FAILURE
            else -> BuildStatus.PENDING
        }
    } else {
        BuildStatus.PENDING
    }
}

fun buildStatusToString(buildStatus: BuildStatus): String {
    return when (buildStatus) {
        BuildStatus.ABORTED -> "ABORTED"
        BuildStatus.PENDING -> "PENDING"
        BuildStatus.PROGRESS -> "PROGRESS"
        BuildStatus.SUCCESS -> "SUCCESS"
        BuildStatus.FAILURE -> "FAILURE"
        BuildStatus.UNKNOWN -> "UNKNOWN"
    }
}
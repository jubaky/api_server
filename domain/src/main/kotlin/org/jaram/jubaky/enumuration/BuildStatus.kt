package org.jaram.jubaky.enumuration

enum class BuildStatus {
    ABORTED,
    PENDING,
    PROGRESS,
    SUCCESS,
    FAILURE,
    UNKNOWN;
}

fun toBuildStatus(status: String): BuildStatus {
    return when (status.toUpperCase()) {
        "ABORTED" -> BuildStatus.ABORTED
        "PENDING" -> BuildStatus.PENDING
        "PROGRESS" -> BuildStatus.PROGRESS
        "SUCCESS" -> BuildStatus.SUCCESS
        "FAILURE" -> BuildStatus.FAILURE
        else -> BuildStatus.UNKNOWN
    }
}
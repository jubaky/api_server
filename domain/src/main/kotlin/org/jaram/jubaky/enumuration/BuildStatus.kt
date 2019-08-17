package org.jaram.jubaky.enumuration

enum class BuildStatus {
    ABORTED,
    SUCCESS,
    FAILURE,
    UNKNOWN;
}

fun toBuildStatus(status: String?): BuildStatus {
    return when (status) {
        "ABORTED" -> BuildStatus.ABORTED
        "SUCCESS" -> BuildStatus.SUCCESS
        "FAILURE" -> BuildStatus.FAILURE
        else -> BuildStatus.UNKNOWN
    }
}
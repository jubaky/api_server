package org.jaram.jubaky.enumuration

enum class DeployStatus {
    PROGRESS,
    SUCCESS,
    FAILURE,
    UNKNOWN
}

fun toDeployStatus(status: String): DeployStatus {
    return when(status.toUpperCase()) {
        "PROGRESS" -> DeployStatus.PROGRESS
        "SUCCESS" -> DeployStatus.SUCCESS
        "FAILURE" -> DeployStatus.FAILURE
        else -> DeployStatus.UNKNOWN
    }
}

fun deployStatusToString(deployStatus: DeployStatus): String {
    return when(deployStatus) {
        DeployStatus.PROGRESS -> "PROGRESS"
        DeployStatus.SUCCESS -> "SUCCESS"
        DeployStatus.FAILURE -> "FAILURE"
        DeployStatus.UNKNOWN -> "UNKNOWN"
    }
}
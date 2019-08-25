package org.jaram.jubaky.domain.jenkins

data class Job(
    val name: String,
    val applicationId: Int,
    val branch: String,
    val description: String?,
    val buildable: Boolean?,
    val concurrentBuild: Boolean?,
    val healthScore: Int?,
    val lastBuildNumber: Int,
    val lastCompletedBuildNumber: Int,
    val lastFailedBuildNumber: Int,
    val lastStableBuildNumber: Int,
    val lastSuccessfulBuildNumber: Int,
    val lastUnstableBuildNumber: Int,
    val lastUnsuccessfulBuildNumber: Int,
    val buildArgumentList: List<BuildArgument>
)
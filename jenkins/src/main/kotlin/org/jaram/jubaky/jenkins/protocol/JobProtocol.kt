package org.jaram.jubaky.jenkins.protocol

import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.Job


data class JobProtocol(
    val name: String,
    val fullName: String?,
    val displayName: String?,
    val displayNameOrNull: String?,
    val description: String?,
    val buildable: Boolean?,
    val concurrentBuild: Boolean?,
    val healthScore: Int?,
    val healthDescription: String?,
    val inQueue: Boolean?,
    val keepDependencies: Boolean?,
    val lastBuildNumber: Int?,
    val lastCompletedBuildNumber: Int?,
    val lastFailedBuildNumber: Int?,
    val lastStableBuildNumber: Int?,
    val lastSuccessfulBuildNumber: Int?,
    val lastUnstableBuildNumber: Int?,
    val lastUnsuccessfulBuildNumber: Int?,
    val buildArgumentList: List<BuildArgument>
) {
    fun toDomainModel() = Job(
        name = name,
        description = description,
        buildable = buildable,
        concurrentBuild = concurrentBuild,
        healthScore = healthScore,
        lastBuildNumber = lastBuildNumber ?: 0,
        lastCompletedBuildNumber = lastCompletedBuildNumber ?: 0,
        lastFailedBuildNumber = lastFailedBuildNumber ?: 0,
        lastStableBuildNumber = lastStableBuildNumber ?: 0,
        lastSuccessfulBuildNumber = lastSuccessfulBuildNumber ?: 0,
        lastUnstableBuildNumber = lastUnstableBuildNumber ?: 0,
        lastUnsuccessfulBuildNumber = lastUnsuccessfulBuildNumber ?: 0,
        buildArgumentList = buildArgumentList
    )
}
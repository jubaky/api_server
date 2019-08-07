package org.jaram.jubaky.jenkins.protocol

import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.JobSpec

data class JobSpecProtocol(
    val name: String?,
    val fullDisplayName: String?,
    val number: Int?,
    val buildArgumentList: List<BuildArgument>,
    val remoteUrlsList: List<String>?,
    val building: Boolean?,
    val description: String?,
    val duration: Int?,
    val estimatedDuration: Int?,
    val queueId: Int?,
    val keepLog: Boolean?,
    val result: String?,
    val timestamp: Long?
) {
    fun toDomainModel() = JobSpec(
        name = name,
        fullDisplayName = fullDisplayName,
        number = number,
        buildArgumentList = buildArgumentList,
        remoteUrlsList = remoteUrlsList,
        building = building,
        description = description,
        duration = duration,
        estimatedDuration = estimatedDuration,
        result = result,
        timestamp = timestamp
    )
}
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
    val buildDuration: Int?,
    val inQueueDuration: Int?,
    val estimatedDuration: Int?,
    val queueId: Int?,
    val keepLog: Boolean?,
    val result: String?,
    val createTimestamp: Long
) {
    fun toDomainModel() = JobSpec(
        name = name,
        fullDisplayName = fullDisplayName,
        number = number,
        buildArgumentList = buildArgumentList,
        remoteUrlsList = remoteUrlsList,
        building = building,
        description = description,
        buildDuration = buildDuration ?: 0,
        inQueueDuration = inQueueDuration ?: 0,
        estimatedDuration = estimatedDuration ?: 0,
        result = result,
        createTimestamp = createTimestamp
    )
}
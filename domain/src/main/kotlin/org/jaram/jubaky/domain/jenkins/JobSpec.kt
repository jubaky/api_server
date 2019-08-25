package org.jaram.jubaky.domain.jenkins

data class JobSpec(
    val name: String?,
    val fullDisplayName: String?,
    val number: Int?,
    val buildArgumentList: List<BuildArgument>,
    val remoteUrlsList: List<String>?,
    val building: Boolean?,
    val description: String?,
    val buildDuration: Int,
    val inQueueDuration: Int,
    val estimatedDuration: Int,
    val result: String?,
    val createTimestamp: Long
)
package org.jaram.jubaky.protocol

data class JobInfo(
    val id: Int,
    val branch: String,
    val applicationName: String,
    val lastBuildNumber: Int,
    val tag: String
)
package org.jaram.jubaky.domain.checker

import org.jaram.jubaky.enumuration.BuildStatus

data class Build(
    val name: String,
    val branch: String,
    val buildNumber: Int,
    val status: BuildStatus
)
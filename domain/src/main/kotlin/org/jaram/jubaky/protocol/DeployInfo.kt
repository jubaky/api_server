package org.jaram.jubaky.protocol

import org.jaram.jubaky.enumuration.DeployStatus
import org.joda.time.DateTime

data class DeployInfo(
    val id: Int,
    val buildId: Int,
    val branch: String,
    val namespace: String,
    val templateName: String,
    val creatorName: String,
    val createTime: DateTime,
    val status: DeployStatus
)
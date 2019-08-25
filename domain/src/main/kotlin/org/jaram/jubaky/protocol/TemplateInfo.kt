package org.jaram.jubaky.protocol

import org.jaram.jubaky.enumuration.Kind
import org.joda.time.DateTime

data class TemplateInfo(
    val id: Int,
    val name: String,
    val kind: Kind,
    val yaml: String,
    val applicationName: String,
    val createTime: DateTime
)
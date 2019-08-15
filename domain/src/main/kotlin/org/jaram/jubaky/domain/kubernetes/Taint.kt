package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Taint(
    val effect: String?,
    val key: String?,
    val timeAdded: DateTime?,
    val value: String?
)
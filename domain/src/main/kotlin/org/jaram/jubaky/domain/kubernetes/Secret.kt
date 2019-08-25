package org.jaram.jubaky.domain.kubernetes

data class Secret(
    val apiVersion: String?,
    val data: Map<String, ByteArray>?,
    val kind: String?,
    val metadata: Metadata?,
    val stringData: Map<String, String>?,
    val type: String?
)
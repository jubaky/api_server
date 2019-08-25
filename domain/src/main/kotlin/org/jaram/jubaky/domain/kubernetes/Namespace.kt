package org.jaram.jubaky.domain.kubernetes

data class Namespace(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: NamespaceSpec?,
    val status: NamespaceStatus?
) {
    data class NamespaceSpec(
        val finalizers: List<String>?
    )

    data class NamespaceStatus(
        val phase: String?
    )
}
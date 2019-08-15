package org.jaram.jubaky.domain.kubernetes

data class PodTemplate(
    val uid: String?
) {
    data class PodTemplateSpec(
        val metadata: Metadata?,
        val spec: Pod.PodSpec?
    )
}
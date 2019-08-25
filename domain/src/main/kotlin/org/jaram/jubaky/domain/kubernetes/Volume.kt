package org.jaram.jubaky.domain.kubernetes

data class Volume(
    val name: String?,
    val hostPath: HostPathVolumeSource?
) {
    data class VolumeDevice(
        val devicePath: String?,
        val name: String?
    )

    data class VolumeMount(
        val mountPath: String?,
        val mountPropagation: String?,
        val name: String?,
        val readOnly: Boolean?,
        val subPath: String?
    )

    data class HostPathVolumeSource(
        val path: String?,
        val type: String?
    )
}
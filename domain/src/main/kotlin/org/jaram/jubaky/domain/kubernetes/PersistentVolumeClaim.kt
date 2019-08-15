package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class PersistentVolumeClaim(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: PersistentVolumeClaimSpec?,
    val status: PersistentVolumeClaimStatus?
) {
    data class PersistentVolumeClaimSpec(
        val accessModes: List<String>?,
        val dataSource: TypedLocalObjectReference?,
        val resources: Resource?,
        val selector: Selector?,
        val storageClassName: String?,
        val volumeMode: String?,
        val volumeName: String?
    )

    data class PersistentVolumeClaimStatus(
        val accessModes: List<String>?,
        val capacity: Map<String, Resource.Quantity>?,
        val conditions: List<PersistentVolumeClaimCondition>?,
        val phase: String?
    )

    data class TypedLocalObjectReference(
        val apiGroup: String?,
        val kind: String?,
        val name: String?
    )

    data class PersistentVolumeClaimCondition(
        val lastProbeTime: DateTime?,
        val lastTransitionTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )
}
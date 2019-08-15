package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class ReplicaSet(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: ReplicaSetSpec?,
    val status: ReplicaSetStatus?
) {
    data class ReplicaSetSpec(
        val minReadySeconds: Int?,
        val replicas: Int?,
        val selector: Selector?,
        val template: PodTemplate.PodTemplateSpec?
    )

    data class ReplicaSetStatus(
        val availableReplicas: Int?,
        val conditions: List<ReplicaSetCondition>?,
        val fullyLabeledReplicas: Int?,
        val observedGeneration: Long?,
        val readyReplicas: Int?,
        val replicas: Int?
    )

    data class ReplicaSetCondition(
        val lastTransitionTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )
}
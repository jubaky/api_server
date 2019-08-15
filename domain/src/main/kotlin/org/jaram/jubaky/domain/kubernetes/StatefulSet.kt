package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class StatefulSet(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: StatefulSetSpec?,
    val status: StatefulSetStatus?
) {
    data class StatefulSetSpec(
        val podManagementPolicy: String?,
        val replicas: Int?,
        val revisionHistoryLimit: Int?,
        val selector: Selector?,
        val serviceName: String?,
        val template: PodTemplate.PodTemplateSpec?,
        val updateStrategy: StatefulSetUpdateStrategy?,
        val volumeClaimTemplates: List<PersistentVolumeClaim>?
    )

    data class StatefulSetStatus(
        val collisionCount: Int?,
        val conditions: List<StatefulSetCondition>?,
        val currentReplicas: Int?,
        val currentRevision: String?,
        val observedGeneration: Long?,
        val readyReplicas: Int?,
        val replicas: Int?,
        val updateRevision: String?,
        val updatedReplicas: Int?
    )

    data class StatefulSetUpdateStrategy(
        val rollingUpdate: RollingUpdateStatefulSetStrategy?,
        val type: String?
    )

    data class RollingUpdateStatefulSetStrategy(
        val partition: Int?
    )

    data class StatefulSetCondition(
        val lastTransitionTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )
}
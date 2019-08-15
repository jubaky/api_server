package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Deployment(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: DeploymentSpec?,
    val status: DeploymentStatus?
) {
    data class DeploymentSpec(
        val progressDeadlineSeconds: Int?,
        val replicas: Int?,
        val revisionHistoryLimit: Int?,
        val selector: Selector?,
        val strategy: DeploymentStrategy?,
        val template: PodTemplate.PodTemplateSpec?
    )

    data class DeploymentStatus(
        val availableReplicas: Int?,
        val collisionCount: Int?,
        val conditions: List<DeploymentCondition>?,
        val observedGeneration: Long?,
        val readyReplicas: Int?,
        val replicas: Int?,
        val unavailableReplicas: Int?,
        val updatedReplicas: Int?
    )

    data class DeploymentStrategy(
        val rollingUpdate: DeploymentRollingUpdate?,
        val type: String?
    )

    data class DeploymentCondition(
        val lastTransitionTime: DateTime?,
        val lastUpdateTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )

    data class DeploymentRollingUpdate(
        val maxSurge: String?,
        val maxUnavailable: String?
    )
}
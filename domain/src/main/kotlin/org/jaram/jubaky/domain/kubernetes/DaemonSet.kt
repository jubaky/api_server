package org.jaram.jubaky.domain.kubernetes

data class DaemonSet(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: DaemonSetSpec?,
    val status: DaemonSetStatus?
) {
    data class DaemonSetSpec(
        val revisionHistoryLimit: Int?,
        val selector: Selector?,
        val template: PodTemplate.PodTemplateSpec?,
        val templateGeneration: Long?,
        val updateStrategy: DaemonSetUpdateStrategy?
    )

    data class DaemonSetStatus(
        val collisionCount: Int?,
        val conditions: List<DaemonSetCondition>?,
        val currentNumberScheduled: Int?,
        val desiredNumberScheduled: Int?,
        val numberAvailable: Int?,
        val numberMisscheduled: Int?,
        val numberReady: Int?,
        val numberUnavailable: Int?,
        val observedGeneration: Long?,
        val updatedNumberScheduled: Int?
    )

    data class DaemonSetCondition(
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )

    data class DaemonSetUpdateStrategy(
        val rollingUpdate: DaemonSetRollingUpdate?,
        val type: String?
    )

    data class DaemonSetRollingUpdate(
        val maxUnavailable: Any?
    )
}
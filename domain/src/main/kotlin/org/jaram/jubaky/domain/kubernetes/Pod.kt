package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Pod(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: PodSpec?,
    val status: PodStatus?
) {
    data class PodSpec(
//        val activeDeadlineSeconds?,
//        val affinity?,
//        val automountServiceAccountToken?,
        val containers: List<Container>?,
        val dnsConfig: PodDNSConfig?,
        val dnsPolicy: String?,
        val enableServiceLinks: Boolean?,
        val hostAliases: List<HostAlias>?,
        val hostIPC: Boolean?,
        val hostNetwork: Boolean?,
        val hostPID: Boolean?,
        val hostname: String?,
//        val imagePullSecrets?,
        val initContainers: List<Container>?,
        val nodeName: String?,
        val nodeSelector: Map<String, String>?,
        val priority: Int?,
        val priorityClassName: String?,
//        val readinessGates?,
        val restartPolicy: String?,
//        val runtimeClassName?,
        val schedulerName: String?,
        val securityContext: PodSecurityContext?,
        val serviceAccount: String?,
        val serviceAccountName: String?,
        val shareProcessNamespace: Boolean?,
        val subdomain: String?,
        val terminationGracePeriodSeconds: Long?,
        val tolerations: List<Toleration>?,
        val volumes: List<Volume>?
    )

    data class PodStatus(
        val conditions: List<PodCondition>?,
        val containerStatuses: List<Container.ContainerStatus>?,
        val hostIP: String?,
        val initContainerStatuses: List<Container.ContainerStatus>?,
        val message: String?,
        val nominatedNodeName: String?,
        val phase: String?,
        val podIP: String?,
        val qosClass: String?,
        val reason: String?,
        val startTime: DateTime?
    )

    data class PodDNSConfig(
        val nameservers: List<String>?,
        val options: List<PodDNSConfigOption>?,
        val searches: List<String>?
    )

    data class PodDNSConfigOption(
        val name: String?,
        val value: String?
    )

    data class PodSecurityContext(
        val fsGroup: Long?,
        val runAsGroup: Long?,
        val runAsNonRoot: Boolean?,
        val runAsUser: Long?
    )

    data class HostAlias(
        val hostnames: List<String>?,
        val ip: String?
    )

    data class Toleration(
        val effect: String?,
        val key: String?,
        val operator: String?,
        val tolerationSeconds: Long?,
        val value: String?
    )

    data class PodCondition(
        val lastProbeTime: DateTime?,
        val lastTransitionTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )
}
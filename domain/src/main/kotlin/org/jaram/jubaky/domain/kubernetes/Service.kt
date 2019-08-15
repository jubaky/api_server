package org.jaram.jubaky.domain.kubernetes

data class Service(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: ServiceSpec?,
    val status: ServiceStatus?
) {
    data class ServiceSpec(
        val clusterIP: String?,
        val externalIPs: List<String>?,
        val externalName: String?,
        val externalTrafficPolicy: String?,
        val healthCheckNodePort: Int?,
        val loadBalancerIP: String?,
        val loadBalancerSourceRanges: List<String>?,
        val ports: List<ServicePort>?,
        val publishNotReadyAddresses: Boolean?,
        val selector: Map<String, String>?,
        val sessionAffinity: String?,
        val sessionAffinityConfig: SessionAffinityConfig?,
        val type: String?
    )

    data class ServiceStatus(
        val loadBalancer: LoadBalancerStatus?
    )

    data class ServicePort(
        val name: String?,
        val nodePort: Int?,
        val port: Int?,
        val protocol: String?,
        val targetPort: Any?
    )

    data class SessionAffinityConfig(
        val clientIP: ClientIPConfig?
    )

    data class LoadBalancerStatus(
        val ingress: List<LoadBalancerIngress>?
    )

    data class LoadBalancerIngress(
        val hostname: String?,
        val ip: String?
    )

    data class ClientIPConfig(
        val timeoutSeconds: Int?
    )
}
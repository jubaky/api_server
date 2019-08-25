package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Node(
    val apiVersion: String?,
    val kind: String?,
    val metadata: Metadata?,
    val spec: NodeSpec?,
    val status: NodeStatus?
) {
    data class NodeSpec(
        val configSource: NodeConfigSource?,
        val externalID: String?,
        val podCIDR: String?,
        val providerID: String?,
        val taints: List<Taint>?,
        val unschedulable: Boolean?
    )

    data class NodeStatus(
        val addresses: List<NodeAddress>?,
        val allocatable: Map<String, Resource.Quantity>?,
        val capacity: Map<String, Resource.Quantity>?,
        val conditions: List<NodeCondition>?,
        val daemonEndpoints: NodeDaemonEndpoints?,
        val images: List<Container.ContainerImage>?,
        val nodeInfo: NodeSystemInfo?,
        val phase: String?
    )

    data class NodeConfigSource(
        val configMap: ConfigMapNodeConfigSource?
    )

    data class ConfigMapNodeConfigSource(
        val kubeletConfigKey: String?,
        val name: String?,
        val namespace: String?,
        val resourceVersion: String?,
        val uid: String?
    )

    data class NodeAddress(
        val address: String?,
        val type: String?
    )

    data class NodeCondition(
        val lastHeartbeatTime: DateTime?,
        val lastTransitionTime: DateTime?,
        val message: String?,
        val reason: String?,
        val status: String?,
        val type: String?
    )

    data class NodeDaemonEndpoints(
        val kubeletEndpoint: DaemonEndpoint?
    )

    data class DaemonEndpoint(
        val Port: Int?
    )

    data class NodeSystemInfo(
        val architecture: String?,
        val bootID: String?,
        val containerRuntimeVersion: String?,
        val kernelVersion: String?,
        val kubeProxyVersion: String?,
        val kubeletVersion: String?,
        val machineID: String?,
        val operatingSystem: String?,
        val osImage: String?,
        val systemUUID: String?
    )
}
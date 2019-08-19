package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.kubernetes.*

interface KubernetesRepository {

    suspend fun getDaemonSet(daemonSetName: String, namespace: String): DaemonSet

    suspend fun getDaemonSetList(namespace: String? = null): List<DaemonSet>

    suspend fun createDaemonSet(yaml: String, namespace: String): DaemonSet

    suspend fun replaceDaemonSet(name: String, yaml: String, namespace: String): DaemonSet

    suspend fun deleteDaemonSet(daemonSetName: String, namespace: String)

    suspend fun getDeployment(deploymentName: String, namespace: String): Deployment

    suspend fun getDeploymentList(namespace: String? = null): List<Deployment>

    suspend fun createDeployment(yaml: String, namespace: String): Deployment

    suspend fun replaceDeployment(name: String, yaml: String, namespace: String): Deployment

    suspend fun deleteDeployment(deploymentName: String, namespace: String)

    suspend fun getNamespace(namespaceName: String): Namespace

    suspend fun getNamespaceList(): List<Namespace>

    suspend fun createNamespace(yaml: String): Namespace

    suspend fun replaceNamespace(name: String, yaml: String): Namespace

    suspend fun deleteNamespace(namespaceName: String)

    suspend fun getNode(nodeName: String): Node

    suspend fun getNodeList(): List<Node>

    suspend fun deleteNode(nodeName: String)

    suspend fun getPod(podName: String, namespace: String): Pod

    suspend fun getPodList(namespace: String? = null): List<Pod>

    suspend fun getPodLog(podName: String, namespace: String): String

    suspend fun createPod(yaml: String, namespace: String): Pod

    suspend fun replacePod(name: String, yaml: String, namespace: String): Pod

    suspend fun deletePod(podName: String, namespace: String)

    suspend fun getReplicaSet(replicaSetName: String, namespace: String): ReplicaSet

    suspend fun getReplicaSetList(namespace: String? = null): List<ReplicaSet>

    suspend fun createReplicaSet(yaml: String, namespace: String): ReplicaSet

    suspend fun replaceReplicaSet(name: String, yaml: String, namespace: String): ReplicaSet

    suspend fun deleteReplicaSet(replicaSetName: String, namespace: String)

    suspend fun getSecret(secretName: String, namespace: String): Secret

    suspend fun getSecretList(namespace: String? = null): List<Secret>

    suspend fun createSecret(yaml: String, namespace: String): Secret

    suspend fun replaceSecret(name: String, yaml: String, namespace: String): Secret

    suspend fun deleteSecret(secretName: String, namespace: String)

    suspend fun getService(serviceName: String, namespace: String): Service

    suspend fun getServiceList(namespace: String? = null): List<Service>

    suspend fun createService(yaml: String, namespace: String): Service

    suspend fun replaceService(name: String, yaml: String, namespace: String): Service

    suspend fun deleteService(serviceName: String, namespace: String)

    suspend fun getStatefulSet(statefulSetName: String, namespace: String): StatefulSet

    suspend fun getStatefulSetList(namespace: String? = null): List<StatefulSet>

    suspend fun createStatefulSet(yaml: String, namespace: String): StatefulSet

    suspend fun replaceStatefulSet(name: String, yaml: String, namespace: String): StatefulSet

    suspend fun deleteStatefulSet(statefulSetName: String, namespace: String)
}
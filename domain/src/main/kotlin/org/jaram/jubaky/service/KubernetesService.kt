package org.jaram.jubaky.service

import org.jaram.jubaky.domain.kubernetes.*
import org.jaram.jubaky.repository.KubernetesRepository

class KubernetesService(
    private val kubernetesRepository: KubernetesRepository
) {

    suspend fun getDaemonSet(daemonSetName: String, namespace: String): DaemonSet {
        return kubernetesRepository.getDaemonSet(daemonSetName, namespace)
    }

    suspend fun getDaemonSetList(namespace: String? = null): List<DaemonSet> {
        return kubernetesRepository.getDaemonSetList(namespace)
    }

    suspend fun createDaemonSet(yaml: String, namespace: String): DaemonSet {
        return kubernetesRepository.createDaemonSet(yaml, namespace)
    }

    suspend fun replaceDaemonSet(name: String, yaml: String, namespace: String): DaemonSet {
        return kubernetesRepository.replaceDaemonSet(name, yaml, namespace)
    }

    suspend fun deleteDaemonSet(daemonSetName: String, namespace: String) {
        return kubernetesRepository.deleteDaemonSet(daemonSetName, namespace)
    }

    suspend fun getDeployment(deploymentName: String, namespace: String): Deployment {
        return kubernetesRepository.getDeployment(deploymentName, namespace)
    }

    suspend fun getDeploymentList(namespace: String? = null): List<Deployment> {
        return kubernetesRepository.getDeploymentList(namespace)
    }

    suspend fun createDeployment(yaml: String, namespace: String): Deployment {
        return kubernetesRepository.createDeployment(yaml, namespace)
    }

    suspend fun replaceDeployment(name: String, yaml: String, namespace: String): Deployment {
        return kubernetesRepository.replaceDeployment(name, yaml, namespace)
    }

    suspend fun deleteDeployment(deploymentName: String, namespace: String) {
        return kubernetesRepository.deleteDeployment(deploymentName, namespace)
    }

    suspend fun getNamespace(namespaceName: String): Namespace {
        return kubernetesRepository.getNamespace(namespaceName)
    }

    suspend fun getNamespaceList(): List<Namespace> {
        return kubernetesRepository.getNamespaceList()
    }

    suspend fun createNamespace(yaml: String): Namespace {
        return kubernetesRepository.createNamespace(yaml)
    }

    suspend fun replaceNamespace(name: String, yaml: String): Namespace {
        return kubernetesRepository.replaceNamespace(name, yaml)
    }

    suspend fun deleteNamespace(namespaceName: String) {
        return kubernetesRepository.deleteNamespace(namespaceName)
    }

    suspend fun getNode(nodeName: String): Node {
        return kubernetesRepository.getNode(nodeName)
    }

    suspend fun getNodeList(): List<Node> {
        return kubernetesRepository.getNodeList()
    }

    suspend fun deleteNode(nodeName: String) {
        return kubernetesRepository.deleteNode(nodeName)
    }

    suspend fun getPod(podName: String, namespace: String): Pod {
        return kubernetesRepository.getPod(podName, namespace)
    }

    suspend fun getPodList(namespace: String? = null): List<Pod> {
        return kubernetesRepository.getPodList(namespace)
    }

    suspend fun createPod(yaml: String, namespace: String): Pod {
        return kubernetesRepository.createPod(yaml, namespace)
    }

    suspend fun replacePod(name: String, yaml: String, namespace: String): Pod {
        return kubernetesRepository.replacePod(name, yaml, namespace)
    }

    suspend fun deletePod(podName: String, namespace: String) {
        return kubernetesRepository.deletePod(podName, namespace)
    }

    suspend fun getReplicaSet(replicaSetName: String, namespace: String): ReplicaSet {
        return kubernetesRepository.getReplicaSet(replicaSetName, namespace)
    }

    suspend fun getReplicaSetList(namespace: String? = null): List<ReplicaSet> {
        return kubernetesRepository.getReplicaSetList(namespace)
    }

    suspend fun createReplicaSet(yaml: String, namespace: String): ReplicaSet {
        return kubernetesRepository.createReplicaSet(yaml, namespace)
    }

    suspend fun replaceReplicaSet(name: String, yaml: String, namespace: String): ReplicaSet {
        return kubernetesRepository.replaceReplicaSet(name, yaml, namespace)
    }

    suspend fun deleteReplicaSet(replicaSetName: String, namespace: String) {
        return kubernetesRepository.deleteReplicaSet(replicaSetName, namespace)
    }

    suspend fun getSecret(secretName: String, namespace: String): Secret {
        return kubernetesRepository.getSecret(secretName, namespace)
    }

    suspend fun getSecretList(namespace: String? = null): List<Secret> {
        return kubernetesRepository.getSecretList(namespace)
    }

    suspend fun createSecret(yaml: String, namespace: String): Secret {
        return kubernetesRepository.createSecret(yaml, namespace)
    }

    suspend fun replaceSecret(name: String, yaml: String, namespace: String): Secret {
        return kubernetesRepository.replaceSecret(name, yaml, namespace)
    }

    suspend fun deleteSecret(secretName: String, namespace: String) {
        return kubernetesRepository.deleteSecret(secretName, namespace)
    }

    suspend fun getService(serviceName: String, namespace: String): Service {
        return kubernetesRepository.getService(serviceName, namespace)
    }

    suspend fun getServiceList(namespace: String? = null): List<Service> {
        return kubernetesRepository.getServiceList(namespace)
    }

    suspend fun createService(yaml: String, namespace: String): Service {
        return kubernetesRepository.createService(yaml, namespace)
    }

    suspend fun replaceService(name: String, yaml: String, namespace: String): Service {
        return kubernetesRepository.replaceService(name, yaml, namespace)
    }

    suspend fun deleteService(serviceName: String, namespace: String) {
        return kubernetesRepository.deleteService(serviceName, namespace)
    }

    suspend fun getStatefulSet(statefulSetName: String, namespace: String): StatefulSet {
        return kubernetesRepository.getStatefulSet(statefulSetName, namespace)
    }

    suspend fun getStatefulSetList(namespace: String? = null): List<StatefulSet> {
        return kubernetesRepository.getStatefulSetList(namespace)
    }

    suspend fun createStatefulSet(yaml: String, namespace: String): StatefulSet {
        return kubernetesRepository.createStatefulSet(yaml, namespace)
    }

    suspend fun replaceStatefulSet(name: String, yaml: String, namespace: String): StatefulSet {
        return kubernetesRepository.replaceStatefulSet(name, yaml, namespace)
    }

    suspend fun deleteStatefulSet(statefulSetName: String, namespace: String) {
        return kubernetesRepository.deleteStatefulSet(statefulSetName, namespace)
    }
}
package org.jaram.jubaky.service

import org.jaram.jubaky.KuberenetesObjectNotFoundException
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.toKind
import org.jaram.jubaky.protocol.DeployInfo
import org.jaram.jubaky.protocol.NamespaceDeployState
import org.jaram.jubaky.repository.DeployRepository
import org.jaram.jubaky.repository.KubernetesRepository

class DeployService(
    private val deployRepository: DeployRepository,
    private val kubernetesRepository: KubernetesRepository
) {

    fun getRecentDeployList(count: Int, namespace: String? = null): List<DeployInfo> {
        return emptyList()
    }

    fun getDeployLog(deployId: Int): String {
        val deployInfo = deployRepository.getDeployInfo(deployId)

        return kubernetesRepository.getPodLog(deployInfo.name, deployInfo.namespace)
    }

    suspend fun runDeploy(buildId: Int, namespace: String) {
        val deployInfo = deployRepository.getDeployInfo(buildId)

        val yaml = deployInfo.yaml
        val kind = deployInfo.kind

        when (toKind(kind)) {
            Kind.DAEMONSET -> kubernetesRepository.createDaemonSet(yaml, namespace)
            Kind.DEPLOYMENT -> kubernetesRepository.createDeployment(yaml, namespace)
            Kind.REPLICASET -> kubernetesRepository.createReplicaSet(yaml, namespace)
            Kind.SERVICE -> kubernetesRepository.createService(yaml, namespace)
            Kind.STATEFULSET -> kubernetesRepository.createStatefulSet(yaml, namespace)
            else -> throw KuberenetesObjectNotFoundException()
        }

        /**
         * @TODO
         * Handling object when the condition is 'REPLACE'
         */
    }

    fun getDeployStatus(applicationId: Int): List<NamespaceDeployState> {
        return emptyList()
    }
}
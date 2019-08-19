package org.jaram.jubaky.service

import org.jaram.jubaky.KuberenetesObjectNotFoundException
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.toKind
import org.jaram.jubaky.protocol.DeployInfo
import org.jaram.jubaky.protocol.NamespaceDeployState
import org.jaram.jubaky.repository.DeployRepository
import org.jaram.jubaky.repository.KubernetesRepository
import org.jaram.jubaky.repository.TemplateRepository

class DeployService(
    private val deployRepository: DeployRepository,
    private val kubernetesRepository: KubernetesRepository,
    private val templateRepository: TemplateRepository
) {

    suspend fun getRecentDeployList(count: Int, namespace: String? = null): List<DeployInfo> {
        return deployRepository.getRecentDeployList(count, namespace)
    }

    suspend fun getDeployLog(deployId: Int): String {
        val deployInfo = deployRepository.getDeployInfoByDeployId(deployId)

        return kubernetesRepository.getPodLog(deployInfo.applicationName, deployInfo.namespace)
    }

    suspend fun runDeploy(buildId: Int, namespace: String) {
        val deployInfo = deployRepository.getDeployInfoByBuildId(buildId)
        val templateInfo = templateRepository.getTemplateInfo(deployInfo.applicationName)

        val yaml = templateInfo.yaml
        val kind = templateInfo.kind.toString()

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
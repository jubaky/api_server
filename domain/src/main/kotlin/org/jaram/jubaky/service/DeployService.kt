package org.jaram.jubaky.service

import org.jaram.jubaky.KubernetesObjectNotFoundException
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.toKind
import org.jaram.jubaky.protocol.DeployInfo
import org.jaram.jubaky.protocol.NamespaceDeployState
import org.jaram.jubaky.repository.DeployRepository
import org.jaram.jubaky.repository.KubernetesRepository
import org.jaram.jubaky.repository.TemplateRepository

class DeployService(
    private val deployRepository: DeployRepository,
    private val templateRepository: TemplateRepository,
    private val kubernetesRepository: KubernetesRepository
) {

    suspend fun getRecentDeployList(count: Int, namespace: String? = null): List<DeployInfo> {
        return deployRepository.getRecentDeployList(count, namespace)
    }

    suspend fun getDeployInfo(deployId: Int): DeployInfo {
        return deployRepository.getDeployInfoByDeployId(deployId)
    }

    suspend fun getDeployLog(deployId: Int): String {
        val deployInfo = deployRepository.getDeployInfoByDeployId(deployId)

        return kubernetesRepository.getPodLog(deployInfo.applicationName, deployInfo.namespace)
    }

    suspend fun runDeploy(buildId: Int, namespace: String, yaml: String) {
        val deployInfo: DeployInfo = deployRepository.getDeployInfoByBuildId(buildId)

        // Create
        if (deployInfo == null) {
            kubernetesRepository.createObject(buildId, yaml, namespace)
        // Replace
        } else {
            val template = templateRepository.getTemplateInfo(deployInfo.applicationName)

            when (template.kind) {
//            Kind.DAEMONSET -> kubernetesRepository.replaceDaemonSet(deployName, yaml, namespace)
                Kind.DEPLOYMENT -> kubernetesRepository.replaceDeployment(deployInfo)
//            Kind.REPLICASET -> kubernetesRepository.replaceReplicaSet(deployName, yaml, namespace)
//            Kind.SERVICE -> kubernetesRepository.replaceService(deployName, yaml, namespace)
//            Kind.STATEFULSET -> kubernetesRepository.replaceStatefulSet(deployName, yaml, namespace)
                else -> throw KubernetesObjectNotFoundException()
            }
        }
    }

    fun getDeployStatus(applicationId: Int): List<NamespaceDeployState> {
        return emptyList()
    }
}
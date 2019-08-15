package org.jaram.jubaky.service

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
        return ""
    }

    fun runDeploy(buildId: Int, namespace: String) {

    }

    fun getDeployStatus(applicationId: Int): List<NamespaceDeployState> {
        return emptyList()
    }
}
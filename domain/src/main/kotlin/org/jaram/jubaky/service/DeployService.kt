package org.jaram.jubaky.service

import org.jaram.jubaky.repository.DeployRepository
import org.jaram.jubaky.repository.KubernetesRepository

class DeployService(
    private val deployRepository: DeployRepository,
    private val kubernetesRepository: KubernetesRepository
) {

    fun getRecentDeployList(count: Int, namespace: String? = null) {

    }

    fun getDeployInfo(deployId: Int) {

    }

    fun runDeploy(buildId: Int, namespace: String) {

    }

    fun getDeployStatus(applicationId: Int) {

    }
}
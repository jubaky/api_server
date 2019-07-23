package org.jaram.jubaky.kubernetes

import io.kubernetes.client.ApiClient
import io.kubernetes.client.apis.CoreV1Api
import io.kubernetes.client.models.V1Namespace
import io.kubernetes.client.models.V1Pod
import io.kubernetes.client.util.Config
import java.io.File

class KubernetesApi(configFile: File) {

    private val client: ApiClient
    private val api: CoreV1Api

    init {
        val inputStream = configFile.inputStream()

        client = Config.fromConfig(inputStream)

        inputStream.close()

        api = CoreV1Api(client)
    }

    fun getNameSpaceList(): List<V1Namespace> {
        return api.listNamespace(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ).items
    }

    fun getPodList(nameSpace: String? = null): List<V1Pod> {
        if (nameSpace.isNullOrBlank()) {
            return api.listPodForAllNamespaces(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ).items
        }

        return api.listNamespacedPod(
            nameSpace,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ).items
    }

    fun getPod(nameSpace: String, podName: String): V1Pod {
        return api.readNamespacedPod(podName, nameSpace, null, null, null)
    }
}
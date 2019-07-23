package org.jaram.jubaky.kubernetes.repository

import io.kubernetes.client.models.V1Namespace
import io.kubernetes.client.models.V1Pod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaram.jubaky.domain.NameSpace
import org.jaram.jubaky.domain.Pod
import org.jaram.jubaky.kubernetes.KubernetesApi
import org.jaram.jubaky.repository.KubernetesRepository

class KubernetesRepositoryImpl(
    private val api: KubernetesApi
) : KubernetesRepository {

    override suspend fun getNameSpaceList(): List<NameSpace> = withContext(Dispatchers.IO) {
        api.getNameSpaceList().map { it.toDomainModel() }
    }

    override suspend fun getPodList(nameSpace: NameSpace?): List<Pod> = withContext(Dispatchers.IO) {
        api.getPodList(nameSpace?.name).map { it.toDomainModel() }
    }

    override suspend fun getPod(nameSpace: NameSpace, podName: String): Pod = withContext(Dispatchers.IO) {
        api.getPod(nameSpace.name, podName).toDomainModel()
    }

    private fun V1Namespace.toDomainModel() = NameSpace(
        metadata.namespace
    )

    private fun V1Pod.toDomainModel() = Pod(
        metadata.uid,
        metadata.name
    )
}
package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.NameSpace
import org.jaram.jubaky.domain.Pod

interface KubernetesRepository {

    suspend fun getNameSpaceList(): List<NameSpace>

    suspend fun getPodList(nameSpace: NameSpace? = null): List<Pod>

    suspend fun getPod(nameSpace: NameSpace, podName: String): Pod
}
package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.DeployInfo
import org.joda.time.DateTime

interface DeployRepository {
    suspend fun getRecentDeployList(count: Int, namespace: String?): List<DeployInfo>

    suspend fun getDeployInfoByDeployId(deployId: Int): DeployInfo

    suspend fun getDeployInfoByBuildId(buildId: Int): DeployInfo

    suspend fun updateDeployStatus(deployId: Int, status: String, endTime: DateTime)

    suspend fun getUserId(buildId: Int): Int
}
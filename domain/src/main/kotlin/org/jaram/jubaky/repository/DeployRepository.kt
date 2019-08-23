package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.DeployInfo
import org.joda.time.DateTime

interface DeployRepository {

    suspend fun createDeploy(buildId: Int, namespace: String, status: String, applicationId: Int, templateId: Int, creatorId: Int)

    suspend fun checkDeploy(buildId: Int): Boolean

    suspend fun getRecentDeployList(applicationId: Int, userGroupId: Int, count: Int, namespace: String?): List<DeployInfo>

    suspend fun getDeployInfoByDeployId(deployId: Int): DeployInfo

    suspend fun getDeployInfoByBuildId(buildId: Int): DeployInfo

    suspend fun updateDeployStatus(deployId: Int, status: String, endTime: DateTime)

    suspend fun getUserId(buildId: Int): Int
}
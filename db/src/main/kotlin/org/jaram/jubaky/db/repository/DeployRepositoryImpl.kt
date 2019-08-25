package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.DeployDao
import org.jaram.jubaky.protocol.DeployInfo
import org.jaram.jubaky.repository.DeployRepository
import org.joda.time.DateTime

class DeployRepositoryImpl(
    private val deployDao: DeployDao
) : DeployRepository {

    override suspend fun createDeploy(buildId: Int, namespace: String, status: String, applicationId: Int, templateId: Int, creatorId: Int) {
        return deployDao.createDeploy(buildId, namespace, status, applicationId, templateId, creatorId)
    }

    override suspend fun checkDeploy(buildId: Int): Boolean {
        return deployDao.checkDeploy(buildId)
    }

    override suspend fun getRecentDeployList(applicationId: Int, userGroupId: Int, count: Int, namespace: String?): List<DeployInfo> {
        return deployDao.getRecentDeployList(applicationId, userGroupId, count, namespace)
    }

    override suspend fun getDeployInfoByDeployId(deployId: Int): DeployInfo {
        return deployDao.getDeployInfoByDeployId(deployId)
    }

    override suspend fun getDeployInfoByBuildId(buildId: Int): DeployInfo {
        return deployDao.getDeployInfoByBuildId(buildId)
    }

    override suspend fun updateDeployStatus(deployId: Int, status: String, endTime: DateTime) {
        deployDao.updateDeployStatus(deployId, status, endTime)
    }

    override suspend fun getUserId(buildId: Int): Int {
        return deployDao.getUserId(buildId)
    }
}
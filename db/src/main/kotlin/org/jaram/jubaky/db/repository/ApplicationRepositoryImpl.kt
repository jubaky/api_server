package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.ApplicationDao
import org.jaram.jubaky.db.dao.GitDao
import org.jaram.jubaky.domain.Application
import org.jaram.jubaky.repository.ApplicationRepository
import org.joda.time.DateTime

class ApplicationRepositoryImpl(
    private val applicationDao: ApplicationDao,
    private val gitDao: GitDao
) : ApplicationRepository {
    override suspend fun getApplicationList(): List<Application> {
        return applicationDao.getApplicationList()
    }

    override suspend fun getApplicationInfo(applicationId: Int): Application {
        return applicationDao.getApplicationInfo(applicationId)
    }

    override suspend fun getApplicationInfo(applicationName: String): Application {
        return applicationDao.getApplicationInfo(applicationName)
    }

    override suspend fun getGitRepositoryUrl(applicationId: Int): String {
        return gitDao.getGitRepositoryUrl(applicationId)
    }

    override suspend fun getUserId(applicationId: Int): Int {
        return applicationDao.getUserId(applicationId)
    }

    override suspend fun updateApplicationTime(applicationId: Int, time: DateTime) {
        applicationDao.updateApplicationTime(applicationId, time)
    }
}
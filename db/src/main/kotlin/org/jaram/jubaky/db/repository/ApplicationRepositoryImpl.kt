package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.ApplicationDao
import org.jaram.jubaky.domain.Application
import org.jaram.jubaky.repository.ApplicationRepository

class ApplicationRepositoryImpl(
    private val gitDao: ApplicationDao
) : ApplicationRepository {
    override fun getApplicationList(): List<Application> {
        return emptyList()
    }

    override fun getApplicationInfo(applicationId: Int): Application {
        return Application(
            id = 0,
            name = "",
            gitRepositoryUrl = "",
            url = ""
        )
    }

    override fun getGitRepositoryUrl(applicationId: Int): String {
        return ""
    }
}
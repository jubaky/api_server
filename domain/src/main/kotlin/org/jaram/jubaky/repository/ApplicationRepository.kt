package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.Application

interface ApplicationRepository {

    fun getApplicationList(): List<Application>

    fun getApplicationInfo(applicationId: Int): Application

    fun getGitRepositoryUrl(applicationId: Int): String
}
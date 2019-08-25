package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.Application
import org.joda.time.DateTime

interface ApplicationRepository {

    suspend fun getApplicationList(): List<Application>

    suspend fun getApplicationInfo(applicationId: Int): Application

    suspend fun getApplicationInfo(applicationName: String): Application

    suspend fun getGitRepositoryUrl(applicationId: Int): String

    suspend fun getUserId(applicationId: Int): Int

    suspend fun updateApplicationTime(applicationId: Int, time: DateTime)
}
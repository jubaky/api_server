package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB

class GitDao(private val db: DB) {
    suspend fun getGitRepositoryUrl(applicationId: Int): String {
        return ""
    }
}
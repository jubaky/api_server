package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Applications
import org.jetbrains.exposed.sql.select

class GitDao(private val db: DB) {
    suspend fun getGitRepositoryUrl(applicationId: Int): String = db.read {
        Applications.slice(Applications.repositoryAddr).select {
            Applications.id.eq(applicationId)
        }.first()[Applications.repositoryAddr]
    }
}
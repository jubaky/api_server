package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.GitDao
import org.jaram.jubaky.repository.GitRepository

class GitRepositoryImpl(
    private val gitDao: GitDao
) : GitRepository {
    override fun getBranchList(repositoryUrl: String): List<String> {
        return emptyList()
    }
}
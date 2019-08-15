package org.jaram.jubaky.repository

interface GitRepository {

    fun getBranchList(repositoryUrl: String): List<String>
}
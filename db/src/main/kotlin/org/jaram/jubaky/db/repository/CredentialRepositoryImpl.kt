package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.CredentialDao
import org.jaram.jubaky.protocol.CredentialInfo
import org.jaram.jubaky.repository.CredentialRepository

class CredentialRepositoryImpl(
    private val credentialDao: CredentialDao
): CredentialRepository {
    override suspend fun getCredentialList(userId: Int): List<CredentialInfo> {
        return credentialDao.getCredentialList(userId)
    }

    override suspend fun createCredential(userId: Int, userName: String, password: String, key: String) {
        credentialDao.createCredential(userId, userName, password, key)
    }

    override suspend fun deleteCredential(key: String) {
        credentialDao.deleteCredential(key)
    }
}
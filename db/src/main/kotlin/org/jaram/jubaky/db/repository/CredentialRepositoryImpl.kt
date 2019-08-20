package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.CredentialDao
import org.jaram.jubaky.protocol.CredentialInfo
import org.jaram.jubaky.repository.CredentialRepository

class CredentialRepositoryImpl(
    private val credentialDao: CredentialDao
): CredentialRepository {
    override suspend fun getCredentialList(): List<CredentialInfo> {
        return credentialDao.getCredentialList()
    }

    override suspend fun createCredential(userName: String, password: String, key: String) {
        credentialDao.createCredential(userName, password, key)
    }

    override suspend fun deleteCredential(key: String) {
        credentialDao.deleteCredential(key)
    }
}
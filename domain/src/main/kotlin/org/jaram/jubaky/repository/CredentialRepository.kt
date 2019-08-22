package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.CredentialInfo

interface CredentialRepository {
    suspend fun getCredentialList(userId: Int): List<CredentialInfo>

    suspend fun createCredential(userName: String, password: String, key: String)

    suspend fun deleteCredential(key: String)
}
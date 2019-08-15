package org.jaram.jubaky.repository

import org.joda.time.DateTime

interface UserRepository {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String)

    suspend fun deleteUser(userId: Int)

    suspend fun deleteUser(emailId: String)

    suspend fun isValidCredentials(emailId: String, password: ByteArray): Boolean

    suspend fun isDuplicatedEmail(emailId: String): Boolean

    suspend fun updateLastLoginTime(emailId: String, time: DateTime)
}
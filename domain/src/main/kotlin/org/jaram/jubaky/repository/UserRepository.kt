package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.User
import org.joda.time.DateTime

interface UserRepository {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String)

    suspend fun deleteUser(userId: Int)

    suspend fun deleteUser(emailId: String)

    suspend fun isValidCredentials(emailId: String, password: ByteArray): Boolean

    suspend fun isDuplicatedEmail(emailId: String): Boolean

    suspend fun updateLastLoginTime(emailId: String, time: DateTime)

    suspend fun getUserInfo(emailId: String): User

    suspend fun getUserGroupId(groupName: String): Int
}
package org.jaram.jubaky.repository

interface UserRepository {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String)

    suspend fun deleteUser(emailId: String, password: ByteArray)

    suspend fun isUser(emailId: String, password: ByteArray): Boolean

    suspend fun doubleCheckUser(emailId: String): Boolean

    suspend fun updateLastLoginTime(emailId: String)
}
package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.User

interface UserRepository {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String)
    suspend fun deleteUser(emailId: String, password: ByteArray)

    suspend fun loginUser(emailId: String, password: ByteArray, name: String): User
}
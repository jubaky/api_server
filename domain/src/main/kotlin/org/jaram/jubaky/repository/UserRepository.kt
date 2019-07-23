package org.jaram.jubaky.repository

interface UserRepository {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String)
}
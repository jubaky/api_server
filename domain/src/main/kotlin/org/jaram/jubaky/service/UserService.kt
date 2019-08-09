package org.jaram.jubaky.service

import org.jaram.jubaky.domain.User
import org.jaram.jubaky.repository.UserRepository

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        userRepository.registerUser(emailId, password, name)
    }

    suspend fun deleteUser(emailId: String, password: ByteArray) {
        userRepository.deleteUser(emailId, password)
    }

    suspend fun loginUser(emailId: String, password: ByteArray, name: String): User {
        return userRepository.loginUser(emailId, password, name)
    }
}
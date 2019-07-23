package org.jaram.jubaky.service

import org.jaram.jubaky.repository.UserRepository

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        userRepository.registerUser(emailId, password, name)
    }
}
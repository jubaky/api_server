package org.jaram.jubaky.service

import org.jaram.jubaky.AlreadyExistedUserException
import org.jaram.jubaky.IncorrectEmailIdOrPasswordException
import org.jaram.jubaky.IncorrectPasswordException
import org.jaram.jubaky.repository.UserRepository
import org.joda.time.DateTime

class UserService(
    private val userRepository: UserRepository,
    private val tokenService: TokenService
) {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        if (userRepository.isDuplicatedEmail(emailId)) {
            throw AlreadyExistedUserException()
        }

        userRepository.registerUser(emailId, password, name)
    }

    suspend fun deleteUser(userId: Int) {
        userRepository.deleteUser(userId)
    }

    suspend fun deleteUser(emailId: String, password: ByteArray) {
        if (!userRepository.isValidCredentials(emailId, password)) {
            throw IncorrectPasswordException()
        }

        userRepository.deleteUser(emailId)
    }

    suspend fun loginUser(emailId: String, password: ByteArray, name: String): Map<String, Any> {
        if (!userRepository.isValidCredentials(emailId, password)) {
            throw IncorrectEmailIdOrPasswordException()
        }

        userRepository.updateLastLoginTime(emailId, DateTime.now())

        val token = tokenService.createToken(emailId, name)

        return mapOf("emailId" to emailId, "name" to name, "token" to token)
    }
}
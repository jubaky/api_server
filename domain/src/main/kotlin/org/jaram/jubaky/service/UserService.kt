package org.jaram.jubaky.service

import org.jaram.jubaky.AlreadyExistedUserException
import org.jaram.jubaky.NonExistedUserException
import org.jaram.jubaky.IncorrectEmailIdOrPasswordException
import org.jaram.jubaky.IncorrectPasswordException
import org.jaram.jubaky.repository.UserRepository

class UserService(
    private val userRepository: UserRepository,
    private val tokenService: TokenService
) {

    suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        if (!userRepository.doubleCheckUser(emailId))
            throw AlreadyExistedUserException()
        userRepository.registerUser(emailId, password, name)
    }

    suspend fun deleteUser(emailId: String, password: ByteArray) {
        if (!userRepository.doubleCheckUser(emailId))
            throw IncorrectPasswordException()
        else if (!userRepository.deleteUser(emailId, password))
            throw NonExistedUserException()
    }

    suspend fun loginUser(emailId: String, password: ByteArray, name: String): Map<String, Any> {
        var token: String
        val userInfo: Map<String, Any>

        if (userRepository.isUser(emailId, password))
            throw IncorrectEmailIdOrPasswordException()

        userRepository.updateLastLoginTime(emailId)
        token = tokenService.createToken(emailId, name)
        userInfo = mapOf("emailId" to emailId, "name" to name, "token" to token)

        return userInfo
    }
}
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
        if (userRepository.doubleCheckUser(emailId))
            userRepository.registerUser(emailId, password, name)
        else
            throw AlreadyExistedUserException()
    }

    suspend fun deleteUser(emailId: String, password: ByteArray) {
        if (!userRepository.isUser(emailId, password))
            userRepository.deleteUser(emailId, password)
        else if (!userRepository.doubleCheckUser(emailId))
            throw IncorrectPasswordException()
        else
            throw NonExistedUserException()
    }

    suspend fun loginUser(emailId: String, password: ByteArray, name: String): List<Any> {
        var token: String
        val userInfo: List<Any>

        if (userRepository.isUser(emailId, password))
            throw IncorrectEmailIdOrPasswordException()

        userRepository.updateLastLoginTime(emailId)
        token = tokenService.createToken(emailId, name)
        userInfo = listOf(emailId, name, token)

        return userInfo
    }
}
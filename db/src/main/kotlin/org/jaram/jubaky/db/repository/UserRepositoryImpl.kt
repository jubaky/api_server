package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.UserDao
import org.jaram.jubaky.domain.User
import org.jaram.jubaky.repository.UserRepository
import org.joda.time.DateTime

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        val token: String
        val expiredTime: DateTime
        if (!userDao.isUser(emailId, password)) {
            userDao.createUser(emailId, password, name)
            token = userDao.generateToken(emailId, name)
            expiredTime = userDao.getExpiration()
            userDao.storeToken(token, emailId, expiredTime)
        } else {
            // already exits
            println("already exits")
        }
    }

    override suspend fun deleteUser(emailId: String, password: ByteArray) {
        if (!userDao.isUser(emailId, password)) {
            userDao.removeToken(emailId)
            userDao.removeUser(emailId, password)
        }
    }

    override suspend fun loginUser(emailId: String, password: ByteArray, name: String): User {
        val token: String
        val expiredTime: DateTime

        if (!userDao.isUser(emailId, password)) {
            // not user
            println("wrong email or password")
            return User(
                emailId = "",
                password = "".toByteArray(),
                name = "",
                token = ""
            )
        }

        token = userDao.generateToken(emailId, name)
        expiredTime = userDao.getExpiration()
        userDao.setLastLoginTime(emailId)
        userDao.storeToken(token, emailId, expiredTime)

        return User(
            emailId = emailId,
            password = password,
            name = name,
            token = token
        )
    }
}
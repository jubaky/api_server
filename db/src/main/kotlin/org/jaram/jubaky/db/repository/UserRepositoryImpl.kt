package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.UserDao
import org.jaram.jubaky.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        userDao.createUser(emailId, password, name)
    }

    override suspend fun deleteUser(emailId: String, password: ByteArray) {
        userDao.removeUser(emailId, password)
    }

    override suspend fun isUser(emailId: String, password: ByteArray): Boolean {
        return userDao.isUser(emailId, password)
    }

    override suspend fun doubleCheckUser(emailId: String): Boolean {
        return userDao.doubleCheckEmailId(emailId)
    }

    override suspend fun updateLastLoginTime(emailId: String) {
        userDao.updateLastLoginTime(emailId)
    }
}
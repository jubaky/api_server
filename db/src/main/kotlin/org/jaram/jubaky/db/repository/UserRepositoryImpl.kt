package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.UserDao
import org.jaram.jubaky.repository.UserRepository
import org.joda.time.DateTime

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        userDao.createUser(emailId, password, name)
    }

    override suspend fun deleteUser(userId: Int) {
        return userDao.disableUser(userId)
    }

    override suspend fun deleteUser(emailId: String) {
        return userDao.disableUser(emailId)
    }

    override suspend fun isValidCredentials(emailId: String, password: ByteArray): Boolean {
        return userDao.isValidCredentials(emailId, password)
    }

    override suspend fun isDuplicatedEmail(emailId: String): Boolean {
        return userDao.isDuplicatedEmail(emailId)
    }

    override suspend fun updateLastLoginTime(emailId: String, time: DateTime) {
        userDao.updateLastLoginTime(emailId, time)
    }
}
package org.jaram.jubaky.db.repository

import org.jaram.jubaky.IncorrectEmailIdOrPasswordException
import org.jaram.jubaky.db.dao.UserDao
import org.jaram.jubaky.domain.User
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
        if(!userDao.isDuplicatedEmail(emailId))
            throw IncorrectEmailIdOrPasswordException()
        return userDao.isValidCredentials(emailId, password)
    }

    override suspend fun isDuplicatedEmail(emailId: String): Boolean {
        return userDao.isDuplicatedEmail(emailId)
    }

    override suspend fun updateLastLoginTime(emailId: String, time: DateTime) {
        userDao.updateLastLoginTime(emailId, time)
    }

    override suspend fun getUserInfo(userId: Int): User {
        return userDao.getUserInfo(userId)
    }

    override suspend fun getUserGroupId(groupName: String): Int {
        return userDao.getUserGroupId(groupName)
    }

    override suspend fun getUserId(emailId: String): Int {
        return userDao.getUserId(emailId)
    }
}
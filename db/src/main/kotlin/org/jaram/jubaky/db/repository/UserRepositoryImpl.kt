package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.UserDao
import org.jaram.jubaky.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun registerUser(emailId: String, password: ByteArray, name: String) {
        userDao.createUser(emailId, password, name)
    }
}
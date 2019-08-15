package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.DeployDao
import org.jaram.jubaky.repository.DeployRepository

class DeployRepositoryImpl(
    private val deployDao: DeployDao
) : DeployRepository {
}
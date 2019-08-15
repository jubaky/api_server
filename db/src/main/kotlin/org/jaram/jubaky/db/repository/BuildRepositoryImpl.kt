package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.BuildDao
import org.jaram.jubaky.repository.BuildRepository

class BuildRepositoryImpl(
    private val buildDao: BuildDao
) : BuildRepository {
}
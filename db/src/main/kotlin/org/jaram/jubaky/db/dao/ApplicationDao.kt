package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.*
import org.jaram.jubaky.domain.Application
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class ApplicationDao(private val db: DB) {
    suspend fun getApplicationList(): List<Application> = db.read {
        Applications.selectAll().map {
            Application (
                id = it[Applications.id].value,
                name = it[Applications.name],
                gitRepositoryUrl = it[Applications.repositoryAddr],
                /** Need to put correct value */
                url = ""
            )
        }
    }

    suspend fun getApplicationInfo(applicationId: Int): Application = db.read {
        Applications.select{ Applications.id.eq(applicationId) }.map {
            Application(
                id = it[Applications.id].value,
                name = it[Applications.name],
                gitRepositoryUrl = it[Applications.repositoryAddr],
                /** Need to put correct value */
                url = ""
            )
        }.first()
    }

    suspend fun getApplicationInfo(applicationName: String): Application = db.read {
        Applications.select{ Applications.name.eq(applicationName) }.map {
            Application(
                id = it[Applications.id].value,
                name = it[Applications.name],
                gitRepositoryUrl = it[Applications.repositoryAddr],
                /** Need to put correct value */
                url = ""
            )
        }.first()
    }

    suspend fun getUserId(applicationId: Int): Int = db.read {
        Applications.innerJoin(Permissions).innerJoin(Groups).innerJoin(GroupMembers).innerJoin(Users).select {
            Applications.id.eq(applicationId)
        }.map { it[Users.id].value }.first()
    }

    suspend fun updateApplicationTime(applicationId: Int, time: DateTime) {
        db.execute {
            Applications.update (
                where = { Applications.id.eq(applicationId) },
                body = {
                    it[this.updateTime] = time
                }
            )
        }
    }
}
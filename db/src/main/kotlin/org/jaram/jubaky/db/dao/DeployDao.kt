package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.*
import org.jaram.jubaky.enumuration.DeployStatus
import org.jaram.jubaky.protocol.DeployInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class DeployDao(private val db: DB) {
    suspend fun createDeploy(buildId: Int, namespace: String, status: String, templateId: Int, creatorId: Int) {
        db.execute {
            Deploys.insert {
                it[this.build] = EntityID(buildId, Builds)
                it[this.namespace] = namespace
                it[this.status] = status
                it[this.template] = EntityID(templateId, Templates)
                it[this.creator] = EntityID(creatorId, Users)
                it[this.createTime] = DateTime.now()
            }
        }
    }

    suspend fun getRecentDeployList(count: Int, namespace: String?): List<DeployInfo> {
        if (namespace == null)
            return db.read {
                Deploys.innerJoin(Users).innerJoin(Applications).innerJoin(Templates).innerJoin(Builds).selectAll()
                    .map {
                        DeployInfo (
                            id = it[Deploys.id].value,
                            applicationName = it[Applications.name],
                            buildId = it[Builds.id].value,
                            branch = it[Builds.branch],
                            namespace = it[Deploys.namespace],
                            templateName = it[Templates.name],
                            creatorName = it[Users.name],
                            createTime = it[Deploys.createTime],
                            /** Need to put correct value **/
                            status = DeployStatus.SUCCESS
                        )
                    }.subList(0, count)
            }
        return db.read {
            Deploys.innerJoin(Users).innerJoin(Applications).innerJoin(Templates).innerJoin(Builds).select{
                Deploys.namespace.eq(namespace)
            }.map {
                    DeployInfo (
                        id = it[Deploys.id].value,
                        applicationName = it[Applications.name],
                        buildId = it[Builds.id].value,
                        branch = it[Builds.branch],
                        namespace = it[Deploys.namespace],
                        templateName = it[Templates.name],
                        creatorName = it[Users.name],
                        createTime = it[Deploys.createTime],
                        /** Need to put correct value **/
                        status = DeployStatus.SUCCESS
                    )
                }.subList(0, count)
        }
    }

    suspend fun getDeployInfoByDeployId(deployId: Int): DeployInfo = db.read {
        Deploys.innerJoin(Users).innerJoin(Applications).innerJoin(Templates).innerJoin(Builds).select{
            Deploys.id.eq(deployId)
        }.map {
            DeployInfo (
                id = it[Deploys.id].value,
                applicationName = it[Applications.name],
                buildId = it[Builds.id].value,
                branch = it[Builds.branch],
                namespace = it[Deploys.namespace],
                templateName = it[Templates.name],
                creatorName = it[Users.name],
                createTime = it[Deploys.createTime],
                /** Need to put correct value **/
                status = DeployStatus.SUCCESS
            )
        }.first()
    }

    suspend fun getDeployInfoByBuildId(buildId: Int): DeployInfo = db.read {
        Deploys.innerJoin(Users).innerJoin(Applications).innerJoin(Templates).innerJoin(Builds).select{
            Deploys.build.eq(buildId)
        }.map {
            DeployInfo (
                id = it[Deploys.id].value,
                applicationName = it[Applications.name],
                buildId = it[Builds.id].value,
                branch = it[Builds.branch],
                namespace = it[Deploys.namespace],
                templateName = it[Templates.name],
                creatorName = it[Users.name],
                createTime = it[Deploys.createTime],
                /** Need to put correct value **/
                status = DeployStatus.SUCCESS
            )
        }.first()
    }

    suspend fun updateDeployStatus(deployId: Int, status: String, endTime: DateTime) {
        db.execute {
            Deploys.update(
                where = { Deploys.id.eq(deployId) },
                body = {
                    it[this.status] = status
                    it[this.finishTime] = endTime
                }
            )
        }
    }

    suspend fun getUserId(buildId: Int): Int = db.read {
        Deploys.innerJoin(Builds).innerJoin(Users).select{
            Deploys.build.eq(buildId)
        }.map { it[Users.id].value }.first()
    }
}
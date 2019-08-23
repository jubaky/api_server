package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.*
import org.jaram.jubaky.enumuration.toDeployStatus
import org.jaram.jubaky.protocol.DeployInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.joda.time.DateTime

class DeployDao(private val db: DB) {
    suspend fun createDeploy(buildId: Int, namespace: String, status: String, applicationId: Int, templateId: Int, creatorId: Int) {
        db.execute {
            Deploys.insert {
                it[this.build] = EntityID(buildId, Builds)
                it[this.namespace] = namespace
                it[this.status] = status
                it[this.application] = EntityID(applicationId, Applications)
                it[this.template] = EntityID(templateId, Templates)
                it[this.creator] = EntityID(creatorId, Users)
                it[this.createTime] = DateTime.now()
            }
        }
    }

    suspend fun checkDeploy(buildId: Int): Boolean = db.read {
        !Deploys.select {
            Deploys.build.eq(buildId)
        }.empty()
    }

    suspend fun getRecentDeployList(userGroupId: Int, count: Int, namespace: String?): List<DeployInfo> {
        if (namespace == null)
            return db.read {
                Deploys.join(Builds, JoinType.INNER, additionalConstraint = {Builds.id.eq(Deploys.build)})
                    .join(Users, JoinType.INNER, additionalConstraint = {Users.id.eq(Deploys.creator)})
                    .join(Applications, JoinType.INNER, additionalConstraint = {Applications.id.eq(Deploys.application)})
                    .join(Templates, JoinType.INNER, additionalConstraint = {Templates.id.eq(Deploys.template)})
                    .join(Permissions, JoinType.INNER, additionalConstraint = {Permissions.application.eq(Deploys.application)})
                    .slice(Deploys.id, Applications.name, Builds.id, Builds.branch, Deploys.namespace, Templates.name, Users.name, Deploys.createTime, Deploys.status)
                    .select {
                    Permissions.groupId.eq(userGroupId)
                }.orderBy(Deploys.createTime to SortOrder.DESC)
                    .limit(count)
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
                            status = toDeployStatus(it[Deploys.status])
                        )
                    }
            }
        return db.read {
            Deploys.join(Builds, JoinType.INNER, additionalConstraint = {Builds.id.eq(Deploys.build)})
                .join(Users, JoinType.INNER, additionalConstraint = {Users.id.eq(Deploys.creator)})
                .join(Applications, JoinType.INNER, additionalConstraint = {Applications.id.eq(Deploys.application)})
                .join(Templates, JoinType.INNER, additionalConstraint = {Templates.id.eq(Deploys.template)})
                .join(Permissions, JoinType.INNER, additionalConstraint = {Permissions.application.eq(Deploys.application)})
                .slice(Deploys.id, Applications.name, Builds.id, Builds.branch, Deploys.namespace, Templates.name, Users.name, Deploys.createTime, Deploys.status)
                .select{
                Deploys.namespace.eq(namespace) and Permissions.groupId.eq(userGroupId)
            }.orderBy(Deploys.createTime to SortOrder.DESC).limit(count).map {
                    DeployInfo (
                        id = it[Deploys.id].value,
                        applicationName = it[Applications.name],
                        buildId = it[Builds.id].value,
                        branch = it[Builds.branch],
                        namespace = it[Deploys.namespace],
                        templateName = it[Templates.name],
                        creatorName = it[Users.name],
                        createTime = it[Deploys.createTime],
                        status = toDeployStatus(it[Deploys.status])
                    )
                }
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
                status = toDeployStatus(it[Deploys.status])
            )
        }.first()
    }

    suspend fun getDeployInfoByBuildId(buildId: Int): DeployInfo = db.read {
        Deploys.innerJoin(Users).innerJoin(Applications).innerJoin(Builds).innerJoin(Templates).select{
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
                status = toDeployStatus(it[Deploys.status])
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
        }.first()[Users.id].value
    }
}
package org.jaram.jubaky.db.dao

import org.jaram.jubaky.db.DB
import org.jaram.jubaky.db.table.Applications
import org.jaram.jubaky.db.table.Templates
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.protocol.TemplateInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

class TemplateDao(private val db: DB) {
    suspend fun createTemplate(name: String, kind: String, content: String, applicationId: Int) {
        db.execute {
            Templates.insert {
                it[this.name] = name
                it[this.kind] = kind
                it[this.yaml] = content
                it[this.application] = EntityID(applicationId, Applications)
            }
        }
    }

    suspend fun getTemplateInfo(applicationName: String): TemplateInfo = db.read {
        Templates.innerJoin(Applications).select {
            Applications.name.eq(applicationName)
        }.map {
            TemplateInfo (
                id = it[Templates.id].value,
                name = it[Templates.name],
                /** Need to put correct value **/
                kind = Kind.DEPLOYMENT,
                yaml = it[Templates.yaml],
                applicationName = it[Applications.name],
                createTime = it[Templates.createTime]
            )
        }.first()
    }

    suspend fun updateTemplateTime(templateId: Int, time: DateTime) {
        db.execute {
            Templates.update (
                where = { Templates.id.eq(templateId) },
                body = {
                    it[this.updateTime] = time
                }
            )
        }
    }
}
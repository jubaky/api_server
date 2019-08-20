package org.jaram.jubaky.db.repository

import org.jaram.jubaky.db.dao.TemplateDao
import org.jaram.jubaky.protocol.TemplateInfo
import org.jaram.jubaky.repository.TemplateRepository
import org.joda.time.DateTime

class TemplateRepositoryImpl(
    private val templateDao : TemplateDao
): TemplateRepository {

    override suspend fun createTemplate(name: String, kind: String, content: String, applicationId: Int) {
        return templateDao.createTemplate(name, kind, content, applicationId)
    }

    override suspend fun getTemplateInfo(applicationName: String): TemplateInfo {
        return templateDao.getTemplateInfo(applicationName)
    }

    override suspend fun updateTemplateTime(templateId: Int, time: DateTime) {
        templateDao.updateTemplateTime(templateId, time)
    }
}
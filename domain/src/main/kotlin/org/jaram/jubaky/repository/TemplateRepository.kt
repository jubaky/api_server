package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.TemplateInfo
import org.joda.time.DateTime

interface TemplateRepository {

    suspend fun createTemplate(name: String, kind: String, content: String, applicationId: Int)

    suspend fun getTemplateInfo(applicationName: String): TemplateInfo

    suspend fun updateTemplateTime(templateId: Int, time: DateTime)
}
package org.jaram.jubaky.repository

import org.jaram.jubaky.protocol.TemplateInfo
import org.joda.time.DateTime

interface TemplateRepository {
    suspend fun getTemplateInfo(applicationName: String): TemplateInfo

    suspend fun updateTemplateTime(templateId: Int, time: DateTime)
}
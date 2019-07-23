package org.jaram.jubaky.repository

import org.jaram.jubaky.domain.Pipeline

interface JenkinsRepository {

    suspend fun startPipeline(pipeline: Pipeline)
}
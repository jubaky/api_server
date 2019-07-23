package org.jaram.jubaky.presenter.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.ContentConverter
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.jvm.javaio.toInputStream
import org.jaram.jubaky.protocol.BaseResponse


class ResponseConverter(private val objectmapper: ObjectMapper = jacksonObjectMapper()) : ContentConverter {

    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? {
        val response = if (value !is BaseResponse<*>) {
            BaseResponse(data = value)
        } else {
            value
        }

        return TextContent(objectmapper.writeValueAsString(response), contentType.withCharset(charset("UTF-8")))
    }

    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val type = request.type
        val value = request.value as? ByteReadChannel ?: return null
        val reader = value.toInputStream().reader(context.call.request.contentCharset() ?: Charsets.UTF_8)
        return objectmapper.readValue(reader, type.javaObjectType)
    }
}
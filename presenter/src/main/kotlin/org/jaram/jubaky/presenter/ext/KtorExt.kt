package org.jaram.jubaky.presenter.ext

import io.ktor.application.ApplicationCall
import io.ktor.http.Parameters
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import org.jaram.jubaky.MissingParameterException
import org.jaram.jubaky.presenter.RECEIVED_PARAMETERS
import org.jaram.jubaky.protocol.BaseResponse

inline val PipelineContext<*, ApplicationCall>.pathParam: Parameters get() = context.parameters

inline val PipelineContext<*, ApplicationCall>.queryParam: Parameters get() = context.request.queryParameters

suspend fun PipelineContext<*, ApplicationCall>.bodyParam(): Parameters? {
    var cache = context.attributes.getOrNull(RECEIVED_PARAMETERS)

    if (cache == null) {
        context.receiveOrNull<Parameters>()?.let {
            context.attributes.put(RECEIVED_PARAMETERS, it)
            cache = it
        }
    }

    return cache
}

fun PipelineContext<*, ApplicationCall>.pathParamSafe(key: String, default: String? = null): String? {
    return context.parameters[key] ?: default
}

fun PipelineContext<*, ApplicationCall>.pathParam(key: String, default: String? = null): String {
    return pathParamSafe(key, default) ?: throw MissingParameterException("required $key")
}

fun PipelineContext<*, ApplicationCall>.queryParamSafe(key: String, default: String? = null): String? {
    return context.request.queryParameters[key] ?: default
}

fun PipelineContext<*, ApplicationCall>.queryParam(key: String, default: String? = null): String {
    return queryParamSafe(key, default) ?: throw MissingParameterException("required $key")
}

suspend fun PipelineContext<*, ApplicationCall>.bodyParamSafe(key: String, default: String? = null): String? {
    return bodyParam()?.get(key) ?: default
}

suspend fun PipelineContext<*, ApplicationCall>.bodyParam(key: String, default: String? = null): String {
    return bodyParamSafe(key, default) ?: throw MissingParameterException("required $key")
}

suspend fun PipelineContext<*, ApplicationCall>.response(value: Any?) {
    if (value == null || value is Unit) {
        context.respond(BaseResponse<Any>())
    }

    context.respond(value!!)
}
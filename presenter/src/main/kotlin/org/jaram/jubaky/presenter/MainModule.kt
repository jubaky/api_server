package org.jaram.jubaky.presenter

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.acceptLanguage
import io.ktor.request.path
import io.ktor.response.respond
import org.jaram.jubaky.ApiException
import org.jaram.jubaky.UnhandledException
import org.jaram.jubaky.presenter.converter.ResponseConverter
import org.jaram.jubaky.util.Jackson
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*

fun Application.serverModule() {

    val errorLogger = LoggerFactory.getLogger("kr.co.coinone.ErrorLogger")

    install(XForwardedHeaderSupport)

    install(CallLogging) {
        level = Level.INFO

        mdc("path") { it.request.path() }
        mdc("clientIp") { it.request.origin.remoteHost }
    }

    install(AutoHeadResponse)

    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            ResponseConverter(Jackson.mapper)
        )
    }

    install(StatusPages) {
        status(HttpStatusCode.InternalServerError) {
            val acceptLanguage = this.context.request.acceptLanguage()
            val locale = if (acceptLanguage == null || acceptLanguage.contains("KR", ignoreCase = true)) {
                Locale.KOREA
            } else {
                Locale.ENGLISH
            }

            call.respond(HttpStatusCode.InternalServerError, UnhandledException(Exception()).toResponse(locale))
        }
        exception<Throwable> {
            throw UnhandledException(it, it.message)
        }
        exception<ApiException> {
            val acceptLanguage = this.context.request.acceptLanguage()
            val locale = if (acceptLanguage == null || acceptLanguage.contains("KR", ignoreCase = true)) {
                Locale.KOREA
            } else {
                Locale.ENGLISH
            }

            if (it.code < 3) {
                errorLogger.error(it.message, it)
            }
            if (it.code != 6 && (it.code < 100 || it.code >= 1000)) {
                errorLogger.warn(it.message, it)
            } else {
                errorLogger.info(it.message, it)
            }

            call.respond(HttpStatusCode.fromValue(it.httpStatus), it.toResponse(locale))
        }
    }
}
package org.jaram.jubaky.presenter

import com.auth0.jwt.JWTVerifier
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.acceptLanguage
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.jaram.jubaky.ApiException
import org.jaram.jubaky.UnhandledException
import org.jaram.jubaky.domain.session.UserSession
import org.jaram.jubaky.presenter.converter.ResponseConverter
import org.jaram.jubaky.util.Jackson
import org.jaram.jubaky.service.TokenService
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.*

private val jwtRealm: String = "jubaky app"
private val jwtAudience: String = "jubaky-audience"

fun Application.serverModule(
    tokenService: TokenService
) {

    val errorLogger = LoggerFactory.getLogger("kr.co.coinone.ErrorLogger")

    val jwtVerifier: JWTVerifier = tokenService.getJWTVerifier()

    install(XForwardedHeaderSupport)

    install(CallLogging) {
        level = Level.INFO

        mdc("path") { it.request.path() }
        mdc("clientIp") { it.request.origin.remoteHost }
    }

    install(CORS) {
        anyHost()
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

    // JWT Authentication Provider
    install(Authentication) {
        jwt(name = "jwtAuth") {
            realm = jwtRealm
            verifier(jwtVerifier)
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

    install(Sessions) {
        cookie<UserSession>("User")
    }
}
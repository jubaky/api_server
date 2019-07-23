package org.jaram.jubaky.presenter

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jaram.jubaky.ext.getInt
import org.jaram.jubaky.presenter.router.user
import org.jaram.jubaky.service.UserService
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

class JubakyServer(
    private val properties: Properties,
    userService: UserService
) {

    val port = properties.getInt("SERVICE_PORT") ?: 8080

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val server: ApplicationEngine

    init {
        server = embeddedServer(Netty, port) {
            serverModule()

            routing {
                get("/health") { call.respondText("ok") }
                route("/user") { user(userService) }
            }
        }
    }

    fun start() {
        logger.info("Started : service_env=${properties["SERVICE_ENV"] ?: "local"}, service_port=$port")

        server.start(wait = true)
    }

    fun stop() {
        server.stop(10, 30, TimeUnit.SECONDS)
    }
}
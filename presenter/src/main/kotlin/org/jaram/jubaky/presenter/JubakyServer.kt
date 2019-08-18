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
import org.jaram.jubaky.presenter.router.app
import org.jaram.jubaky.presenter.router.dev
import org.jaram.jubaky.presenter.router.infra
import org.jaram.jubaky.presenter.router.user
import org.jaram.jubaky.service.*
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

class JubakyServer(
    private val properties: Properties,
    private val buildCheckService: BuildCheckService,
    private val deployCheckService: DeployCheckService,
    userService: UserService,
    tokenService: TokenService,
    jenkinsService: JenkinsService,
    kubernetesService: KubernetesService,
    applicationService: ApplicationService,
    buildService: BuildService,
    deployService: DeployService
) {

    val port = properties.getInt("SERVICE_PORT") ?: 8080

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val server: ApplicationEngine

    init {
        server = embeddedServer(Netty, port) {
            serverModule(tokenService)

            routing {
                get("/health") { call.respondText("ok") }
                route("/user") { user(userService) }
                route("/dev") { dev(jenkinsService) }
                route("/infra") { infra(kubernetesService) }
                route("/app") { app(applicationService, buildService, deployService) }
            }
        }
    }

    fun start() {
        logger.info("Started : service_env=${properties["SERVICE_ENV"] ?: "local"}, service_port=$port")

        buildCheckService.runBuildCheck()
        logger.info("Build checker is started")
        buildCheckService.runPendingBuildCheck()
        logger.info("Pending build checker is started")
        buildCheckService.runCheckHealth()
        logger.info("Build health checker is started")

        deployCheckService.runDeployCheck()
        logger.info("Deploy checker is started")
        deployCheckService.runCheckHealth()
        logger.info("Deploy health checker is started")

        server.start(wait = true)
    }

    fun stop() {
        server.stop(10, 30, TimeUnit.SECONDS)

        buildCheckService.stopBuildCheck()
        logger.info("Build checker is stopped")
        buildCheckService.stopPendingBuildCheck()
        logger.info("Pending build checker is stopped")
        buildCheckService.stopCheckHealth()
        logger.info("Build health checker is stopped")

        deployCheckService.stopDeployCheck()
        logger.info("Deploy checker is stopped")
        deployCheckService.stopCheckHealth()
        logger.info("Deploy health checker is stopped")
    }
}
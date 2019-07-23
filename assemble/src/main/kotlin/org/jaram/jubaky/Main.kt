package org.jaram.jubaky

import org.jaram.jubaky.logger.KoinLogger
import org.jaram.jubaky.module.*
import org.jaram.jubaky.presenter.JubakyServer
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.dsl.onRelease
import org.koin.experimental.builder.single
import java.util.*

fun main() {
    val properties = Properties()

    properties.putAll(System.getProperties())
    properties.putAll(System.getenv())

    val serviceEnv = properties.getProperty("SERVICE_ENV", "local").toLowerCase()

    val commonProperties = JubakyServer::class.java.classLoader.getResourceAsStream("common.properties")
    val envProperties = JubakyServer::class.java.classLoader.getResourceAsStream("$serviceEnv.properties")

    properties.load(commonProperties)
    properties.load(envProperties)

    commonProperties?.close()
    envProperties?.close()


    startKoin {
        logger(KoinLogger())
        koin.propertyRegistry.saveProperties(properties)
        modules(module {
            single { properties }
            single<JubakyServer>().onRelease { it?.stop() }
        })
        modules(
            listOf(
                DataSourceModule,
                DBModule,
                JenkinsModule,
                KubernetesModule,
                RepositoryModule,
                ServiceModule
            )
        )
    }.koin
        .get<JubakyServer>()
        .start()
}
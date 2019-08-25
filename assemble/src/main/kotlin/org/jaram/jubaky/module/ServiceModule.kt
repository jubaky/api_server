package org.jaram.jubaky.module

import org.jaram.jubaky.ext.getInt
import org.jaram.jubaky.ext.getString
import org.jaram.jubaky.service.*
import org.koin.dsl.module
import org.koin.experimental.builder.single

val ServiceModule = module {
    single<UserService>()
    single<JenkinsService>()
    single<KubernetesService>()
    single<ApplicationService>()
    single<DeployService>()
    single<BuildService>()

    single {
        BuildCheckService(
            get(),
            get(),
            get(),
            intervalDelayTime = getInt("jenkins.build.intervalDelayTime") ?: 1000,
            intervalCheckHealthTime = getInt("jenkins.build.intervalCheckHealthTime") ?: 60000
        )
    }

    single {
        DeployCheckService(
            get(),
            intervalDelayTime = getInt("kubernetes.deploy.intervalDelayTime") ?: 1000,
            intervalCheckHealthTime = getInt("kubernetes.deploy.intervalCheckHealthTime") ?: 60000
        )
    }

    single {
        TokenService(
            getString("token.jubaky.jwtIssuer")!!,
            getString("token.jubaky.secret")!!,
            getString("token.jubaky.jwtAudience")!!,
            getInt("token.jubaky.validityInMs")!!
        )
    }
}
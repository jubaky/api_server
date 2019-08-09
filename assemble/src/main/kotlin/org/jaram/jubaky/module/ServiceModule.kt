package org.jaram.jubaky.module

import org.jaram.jubaky.ext.getInt
import org.jaram.jubaky.ext.getString
import org.jaram.jubaky.service.TokenService
import org.jaram.jubaky.service.UserService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.single

val ServiceModule = module {
    single<UserService>()

    single(named("token-service")){
        TokenService(
            getString("token.jubaky.jwtIssuer")!!,
            getString("token.jubaky.secret")!!,
            getString("token.jubaky.jwtAudience")!!,
            getInt("token.jubaky.validityInMs")!!
        )
    }
}
package org.jaram.jubaky.module

import org.jaram.jubaky.service.UserService
import org.koin.dsl.module
import org.koin.experimental.builder.single

val ServiceModule = module {
    single<UserService>()
}
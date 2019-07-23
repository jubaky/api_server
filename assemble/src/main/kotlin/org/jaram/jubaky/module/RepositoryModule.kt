package org.jaram.jubaky.module

import org.jaram.jubaky.db.repository.UserRepositoryImpl
import org.jaram.jubaky.repository.UserRepository
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val RepositoryModule = module {
    singleBy<UserRepository, UserRepositoryImpl>()
}
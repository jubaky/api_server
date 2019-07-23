package org.jaram.jubaky.module

import org.jaram.jubaky.db.dao.UserDao
import org.koin.core.qualifier.named
import org.koin.dsl.module

val DBModule = module {
    single { UserDao(get(named("jubaky-db"))) }
}
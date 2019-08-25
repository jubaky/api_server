package org.jaram.jubaky.module

import org.jaram.jubaky.db.dao.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val DBModule = module {
    single { ApplicationDao(get(named("jubaky-db"))) }
    single { BuildDao(get(named("jubaky-db"))) }
    single { DeployDao(get(named("jubaky-db"))) }
    single { GitDao(get(named("jubaky-db"))) }
    single { UserDao(get(named("jubaky-db"))) }
    single { TemplateDao(get(named("jubaky-db"))) }
    single { CredentialDao(get(named("jubaky-db"))) }
    single { JobDao(get(named("jubaky-db"))) }
}
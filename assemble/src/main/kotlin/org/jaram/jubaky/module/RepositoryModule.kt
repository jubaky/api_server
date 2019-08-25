package org.jaram.jubaky.module

import org.jaram.jubaky.db.repository.*
import org.jaram.jubaky.repository.*
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val RepositoryModule = module {
    singleBy<UserRepository, UserRepositoryImpl>()
    singleBy<ApplicationRepository, ApplicationRepositoryImpl>()
    singleBy<BuildRepository, BuildRepositoryImpl>()
    singleBy<DeployRepository, DeployRepositoryImpl>()
    singleBy<GitRepository, GitRepositoryImpl>()
    singleBy<TemplateRepository, TemplateRepositoryImpl>()
    singleBy<CredentialRepository, CredentialRepositoryImpl>()
    singleBy<JobRepository, JobRepositoryImpl>()
}
package org.jaram.jubaky.module

import org.jaram.jubaky.jenkins.repository.JenkinsRepositoryImpl
import org.jaram.jubaky.repository.JenkinsRepository
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val JenkinsModule = module {
    singleBy<JenkinsRepository, JenkinsRepositoryImpl>()
}
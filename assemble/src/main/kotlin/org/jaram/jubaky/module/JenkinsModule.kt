package org.jaram.jubaky.module

import org.jaram.jubaky.ext.getInt
import org.jaram.jubaky.ext.getString
import org.jaram.jubaky.jenkins.JenkinsClientFactory
import org.jaram.jubaky.jenkins.repository.JenkinsRepositoryImpl
import org.jaram.jubaky.repository.JenkinsRepository
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy


val JenkinsModule = module {
    single {
        JenkinsClientFactory(
            username = getString("jenkins.username")?: "",
            token = getString("jenkins.token")?: "",
            baseUrl = getString("jenkins.url")?: ""
        )
    }

    single {
        get<JenkinsClientFactory>().createJenkinsClientWithJson()
    }

    single {
        get<JenkinsClientFactory>().createJenkinsClientWithText()
    }

    single {
        getInt("jenkins.build.startDelayTime") ?: 200
    }

    singleBy<JenkinsRepository, JenkinsRepositoryImpl>()
}
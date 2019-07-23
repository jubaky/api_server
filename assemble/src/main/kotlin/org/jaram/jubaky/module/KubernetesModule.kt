package org.jaram.jubaky.module

import org.jaram.jubaky.kubernetes.repository.KubernetesRepositoryImpl
import org.jaram.jubaky.repository.KubernetesRepository
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val KubernetesModule = module {
    singleBy<KubernetesRepository, KubernetesRepositoryImpl>()
}
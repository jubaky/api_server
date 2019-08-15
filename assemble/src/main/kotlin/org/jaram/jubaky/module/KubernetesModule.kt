package org.jaram.jubaky.module

import org.jaram.jubaky.ext.getString
import org.jaram.jubaky.kubernetes.KubernetesApi
import org.jaram.jubaky.kubernetes.repository.KubernetesRepositoryImpl
import org.jaram.jubaky.repository.KubernetesRepository
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import java.io.File

val KubernetesModule = module {
    single {
        KubernetesApi(
            File(getString("kubernetes.kubeconfig.path") ?: "")
        )
    }

    singleBy<KubernetesRepository, KubernetesRepositoryImpl>()
}
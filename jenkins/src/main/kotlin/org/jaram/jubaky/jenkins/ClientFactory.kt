package org.jaram.jubaky.jenkins

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit

class ClientFactory(
    private val jenkinsUrl: String
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(jenkinsUrl)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}
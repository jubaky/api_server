package org.jaram.jubaky.jenkins

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class JenkinsClientFactory(
    val username: String,
    val token: String,
    val baseUrl: String
) {

    fun createJenkinsClientWithJson(): JenkinsClientWithJson {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(JenkinsAuthInterceptor(username, token))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(JenkinsClientWithJson::class.java)
    }

    fun createJenkinsClientWithText(): JenkinsClientWithText {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(JenkinsAuthInterceptor(username, token))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(JenkinsClientWithText::class.java)
    }

    inner class JenkinsAuthInterceptor(username: String, password: String) : Interceptor {

        private val credentials = Credentials.basic(username, password)

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials)
                .build()

            return chain.proceed(authenticatedRequest)
        }
    }
}
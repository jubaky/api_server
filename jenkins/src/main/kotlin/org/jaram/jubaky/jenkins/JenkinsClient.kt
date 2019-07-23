package org.jaram.jubaky.jenkins

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JenkinsClient {

    @GET("/")
    fun sample(@Query("queryParam") sample: String): Deferred<Response<String>>
}
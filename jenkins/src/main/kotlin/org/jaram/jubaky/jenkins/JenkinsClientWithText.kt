package org.jaram.jubaky.jenkins

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JenkinsClientWithText {

    @GET("/job/{job_name}/{build_number}/logText/progressiveText")
    fun getJobLog(
        @Path("job_name") jobName: String,
        @Path("build_number") buildNumber: String,
        @Query("start") start: Int = 0
    ): Deferred<Response<String>>
}
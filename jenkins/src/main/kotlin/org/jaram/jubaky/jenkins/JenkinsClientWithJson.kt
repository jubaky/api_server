package org.jaram.jubaky.jenkins

import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface JenkinsClientWithJson {

    @POST("/github-webhook/")
    fun redirectGithubWebhook(
        @HeaderMap headers: Map<String, String>,
        @Body payload: RequestBody
    ): Deferred<Response<Void>>

    @GET("/job/{jobName}/api/json")
    fun getJobInfo(
        @Path("jobName") jobName: String
    ): Deferred<Response<Map<String, Any>>>

    @GET("/job/{jobName}/{build_number}/api/json")
    fun getJobSpecInfo(
        @Path("jobName") jobName: String,
        @Path("build_number") buildNumber: Int
    ): Deferred<Response<Map<String, Any>>>

    @Headers("Content-Type: text/xml")
    @POST("/createItem")
    fun createJob(
        @Query("name") jobName: String,
        @Body file: RequestBody
    ): Deferred<Response<Void>>

    @POST("/job/{jobName}/doDelete")
    fun deleteJob(
        @Path("jobName") jobName: String
    ): Deferred<Response<Void>>

    @POST("/job/{jobName}/config.xml")
    fun updateJob(
        @Path("jobName") jobName: String,
        @Body file: RequestBody
    ): Deferred<Response<Void>>

    @POST("/job/{jobName}/buildWithParameters")
    fun buildWithParameters(
        @Path("jobName") jobName: String,
        @QueryMap queries: Map<String, String>
    ): Deferred<Response<Void>>

    @GET("/queue/api/json")
    fun getPenddingBuildList(): Deferred<Response<Map<String, Any>>>

    @FormUrlEncoded
    @POST("/credentials/store/system/domain/_/createCredentials")
    fun createCredentials(
        @Field("json") credData: String
    ): Deferred<Response<Void>>

    @POST("/credentials/store/system/domain/_/credential/{key}/doDelete")
    fun deleteCredentials(
        @Path("key") key: String
    ): Deferred<Response<Void>>
}

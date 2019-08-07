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

    @GET("/job/{job_name}/api/json")
    fun getJobInfo(
        @Path("job_name") jobName: String
    ): Deferred<Response<Map<String, Any>>>

    @GET("/job/{job_name}/{build_number}/api/json")
    fun getJobSpecInfo(
        @Path("job_name") jobName: String,
        @Path("build_number") buildNumber: String
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

    @POST("/job/{job_name}/buildWithParameters")
    fun buildWithParameters(
        @Path("job_name") jobName: String,
        @QueryMap queries: Map<String, String>
//        @Query("image_username") imageUsername: String,
//        @Query("image_name") imageName: String,
//        @Query("image_version") imageVersion: String,
//        @Query("account_username") accountUsername: String,
//        @Query("account_password") accountPassword: String
    ): Deferred<Response<Void>>

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

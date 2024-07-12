package com.service.composesample.utils

import com.google.gson.JsonObject
import com.service.composesample.model.UserInfo
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface NetworkApiCallInterface {
    @GET
    suspend fun makeHttpGetRequest(@Url url: String): List<UserInfo>
    //for queryparameters and path parameter
    @GET("/{pathId}")
    suspend fun makeHttpGetRequest(@Query("id") id:String,@Path(value = "pathId")pathId:String, @Url url: String): List<UserInfo>
    @GET
    fun makeHttpGetRequestString(@Url url: String): Call<JsonObject>
    @GET
    fun makeHttpGetXmlRequest(@Url url: String): Call<JsonObject>

    @POST
    fun makeHttpPostRequest(@Url url: String, @Body body: HashMap<String, String>): Call<JsonObject>

    @POST
    fun makeHttpJsonPostRequest(@Url url: String, @Body body: HashMap<String, String>): Response<JsonObject>

    @POST
    fun makeHttpJsonPostRequest(@Url url: String, @HeaderMap headers: HashMap<String, String>, @Body body: JsonObject): Response<JsonObject>

    @POST
    fun makeMultipartJsonResRequest(@Url url: String, @Body requestBody: RequestBody): Response<JsonObject>
}

package com.example.melkist.network.`interface`

import com.example.melkist.models.ProvinceCityModel
import com.example.melkist.models.VerificationResponseModel
import retrofit2.http.POST
import retrofit2.http.Query


private const val GIT_INFINITE_TOKEN = "KvFlcTktwviZwDUolajY3x3KsToKfp4XgKLw"


interface ApiService {
    @POST("sendVerificationCode")
    suspend fun getVerificationCode(
        @Query("mobile") query: String
    ): VerificationResponseModel

    @POST("/forgetPassword")
    suspend fun getForgetPasswordVerificationCode(
        @Query("mobile") mobile: String,
        @Query("nationalcode") nationalCode: String
    ): VerificationResponseModel

    @POST("verifyCode")
    suspend fun getVerifyResponse(
        @Query("mobile") mobile: String,
        @Query("code") code: String
    ): VerificationResponseModel

    @POST("getProvinces")
    suspend fun getGetProvinces(): ProvinceCityModel



/*    @GET("users")
    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer ghp_$GIT_INFINITE_TOKEN",
        "X-GitHub-Api-Version: 2022-11-28")
    suspend fun getUserList(): List<GitUser>

    @GET("search/users")
    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer ghp_$GIT_INFINITE_TOKEN",
        "X-GitHub-Api-Version: 2022-11-28")
    suspend fun getSearchUsers(
        @Query("q") query: String
    ): SearchModel

    @GET("users/{username}")
    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer ghp_$GIT_INFINITE_TOKEN",
        "X-GitHub-Api-Version: 2022-11-28")
    suspend fun getUser(
        @Path(value = "username") username: String
    ): UserModel

    @GET("users/{username}/followers")
    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer ghp_$GIT_INFINITE_TOKEN",
        "X-GitHub-Api-Version: 2022-11-28")
    suspend fun getFollower(
        @Path(value = "username") username: String
    ): List<GitUser>

    @GET("users/{username}/following")
    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer ghp_$GIT_INFINITE_TOKEN",
        "X-GitHub-Api-Version: 2022-11-28")
    suspend fun getFollowing(
        @Path(value = "username") username: String
    ): List<GitUser>*/
}
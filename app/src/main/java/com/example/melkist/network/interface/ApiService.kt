package com.example.melkist.network.`interface`

import com.example.melkist.models.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login (
        @Query("mobile") mobile: String,
        @Query("password") password: String,
        @Query("needCaptcha") needCaptcha: Boolean = false // always false in app
    ): LoginResponseModel

    @POST("sendVerificationCode")
    suspend fun getVerificationCode(
        @Query("mobile") query: String
    ): PublicResponseModel

    @POST("forgetPassword")
    suspend fun getForgetPasswordVerificationCode(
        @Query("mobile") mobile: String,
        @Query("nationalcode") nationalCode: String
    ): PublicResponseModel

    @POST("verifyCode")
    suspend fun verifyCode(
        @Query("mobile") mobile: String,
        @Query("code") code: String
    ): PublicResponseModel

    @POST("changePasswordByMobile")
    suspend fun changePasswordByMobile(
        @Query("mobile") mobile: String,
        @Query("newPassword") newPassword: String
    ): PublicResponseModel

    @POST("getProvinces")
    suspend fun getGetProvinces(): PcrsModel

    @POST("getCitiesByProvince")
    suspend fun getCitiesByProvinceId(
        @Query("province_id") provinceId: Int
    ): PcrsModel

    @POST("getRealstatesByCity")
    suspend fun getRealEstateByCityId(
        @Query("city_id") cityId: Int
    ): PcrsModel

    @POST("getSupervisorsByManager")
    suspend fun getSuperVisorByManagerId(
        @Query("manager_id") managerId: Int
    ): PcrsModel

    @POST("checkSignupData")
    suspend fun checkSignupData(
        @Query("name") name: String?,
        @Query("family") lastName: String?,
        @Query("title") title: String?,
        @Query("city_id") cityId: Int?,
        @Query("mobile") mobile: String,
        @Query("nationalcode") nationalCode: String,
        @Query("email") email: String?,
        @Query("role_id") roleId: Int,
    ) : PublicResponseModel

    @POST("registerUserRealstate")
    suspend fun registerUserRealEstate(
        @Query("title") realEstateNameForManager: String?,
        @Query("city_id") cityId: Int,
        @Query("name") name: String,
        @Query("family") lastName: String,
        @Query("mobile") mobile: String,
        @Query("nationalcode") nationalCode: String,
        @Query("email") email: String?,
        @Query("password") password: String,
        @Query("parent_id") parentId: Int,
        @Query("role_id") roleId: Int,
        @Query("isverify") isVerify: Boolean = false, // for check if user create by app or by panel. in app should always be false
        @Query("isNeedValidation") isNeedValidation: Boolean = false
    ) : PublicResponseModel

    @POST ("dashboard/admin/file/getFileCategories")
    suspend fun getFileCategories(
        @Query("fileType_id") typeId: Int
    ): CatSubCatResponse

    @POST ("dashboard/admin/file/getFileCategoryTypes")
    suspend fun getFileCategoryTypes(
        @Query("fileType_id") typeId: Int,
        @Query("fileCategory_id") catId: Int,
    ): CatSubCatResponse

    @POST ("getRegionsByCity")
    suspend fun getRegionsByCity(
        @Query("city_id") cityId: Int
    ): RegionResponseModel

    @POST ("dashboard/admin/file/save")
    suspend fun save(
        @Body img: FileSave
    ): PublicResponseModel

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
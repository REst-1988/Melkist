package com.example.melkist.network.`interface`

import com.example.melkist.models.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("checkAppVersion")
    suspend fun versionControl(
        @Query("user_id") userId: Int?,
        @Query("firebase_token") firebaseToken: String?,
        @Query("app_version") appVersion: String
    ): AppVersionResponse

    @POST("login")
    suspend fun login(
        @Query("mobile") mobile: String,
        @Query("password") password: String,
        @Query("firebase_token") firebaseToken: String?,
        @Query("needCaptcha") needCaptcha: Boolean = false // always false in app
    ): LoginResponseModel

    @POST("dashboard/admin/profile/uploadProfilePic")
    suspend fun uploadProfilePic(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int,
        @Body profilePic: String
    ): PublicResponseModel

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
    ): PublicResponseModel

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
    ): PublicResponseModel

    @POST("dashboard/admin/file/getFileCategories")
    suspend fun getFileCategories(
        @Header("Authorization") token: String,
        @Query("fileType_id") typeId: Int
    ): CatSubCatResponse

    @POST("dashboard/admin/file/getFileCategoryTypes")
    suspend fun getFileCategoryTypes(
        @Header("Authorization") token: String,
        @Query("fileType_id") typeId: Int,
        @Query("fileCategory_id") catId: Int,
    ): CatSubCatResponse

    @POST("getRegionsByCity")
    suspend fun getRegionsByCity(
        @Query("city_id") cityId: Int
    ): RegionResponseModel

    @POST("dashboard/admin/file/save")
    suspend fun saveFile( // TODO: need token?
        @Body img: FileSave
    ): PublicResponseModel

    @POST("dashboard/admin/file/getAllFilesByCity")
    suspend fun getAllFilesByCity(
        @Header("Authorization") token: String,
        @Query ("city_id") cityId: Int
    ): LocationResponse

    @POST("dashboard/admin/file/getFileInfoById")
    suspend fun getFileInfoById(
        @Header("Authorization") token: String,
        @Query("file_id") fileId: Int,
        @Query("user_id") userId: Int
    ): FileDataResponse

    @POST("dashboard/admin/file/delete")
    suspend fun deleteFile(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): PublicResponseModel

    @POST("dashboard/admin/file/saveFavoriteFile")
    suspend fun saveFavoriteFile(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int,
        @Query("file_id") fileId: Int
    ): PublicResponseModel

    @POST("dashboard/admin/file/deleteFavoriteFile")
    suspend fun deleteFavoriteFiles(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int,
        @Query("file_id") fileId: Int
    ): PublicResponseModel

    @POST("dashboard/admin/file/getFavoriteFiles")
    suspend fun getFavoriteFiles(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): FavFileResponse


    @POST("dashboard/admin/file/filterFiles")
    suspend fun filterFiles(
        @Header("Authorization") token: String,
        @Body filerFileData: FilterFileData
    ): LocationResponse

    @POST("dashboard/admin/file/inboxRequest")
    suspend fun inbox(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): InboxOutboxModel

    @POST("dashboard/admin/file/outboxRequest")
    suspend fun outbox(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): InboxOutboxModel

    @POST("dashboard/admin/file/sendCooperationRequest")
    suspend fun sendCooperationRequest( // TODO: Finish this part
        @Header("Authorization") token: String,
        @Query("applicant_id") userId: Int,
        @Query("file_id") fileId: Int
    ): PublicResponseModel// TODO: check this out

    @POST("dashboard/admin/getAllUserSubset")
    suspend fun getAllUserSubset(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int,
        @Query("role_id") roleId: Int
    ): Users

    @POST("dashboard/admin/file/getFileInfoByUserId")
    suspend fun getFileInfoByUserId(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): MyFilesResponse

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
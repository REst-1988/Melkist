package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.Serializable

data class User(
    @Json val id: Int?,
    @Json(name = "name") val firstName: String?,
    @Json(name = "family") val lastName: String?,
    @Json(name = "mobile_id") val mobileId: Int?,
    @Json(name = "email") val email: String?,
    @Json(name = "nationalcode") val nationalCode: Long?,
    @Json(name = "profilepic") val profilePic: String?,
    @Json(name = "isverify") val isVerify: Boolean?,
    @Json(name = "created_at") val createAt: String?,
    @Json(name = "updated_at") val updateAt: String?,
    @Json(name = "role_id") val roleId: Int?,
    @Json(name = "parent_id") val parentId: Int?,
    @Json(name = "isfirsttime") val isFirstTime: Boolean?,
    @Json(name = "mobile") val mobile: Mobile?,
    @Json val role: Role?,
    @Json val cityId: Int?,
    @Json val cityTitle: String?,
    @Json val provinceId: Int?,
    @Json val provinceTitle: String?,
    @Json(name = "realstate") val realEstate: String?,
    @Json val token: String?
): Serializable

data class Users(
    @Json val result: Boolean?, @Json val data: List<User>?, @Json val errors: List<String>?
)

data class Mobile(
    @Json val id: Int?, @Json val mobile: String?
): Serializable

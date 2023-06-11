package com.example.melkist.models

import com.squareup.moshi.Json

data class LoginResponseModel(
    @Json var result: Boolean?,
    @Json var data: UserResponseModel?,
    @Json var token: String?,
    @Json var errors: List<String>? = listOf()
)

data class UserResponseModel(
    @Json val id: Int?,
    @Json(name = "name") val firstName: String?,
    @Json(name = "family") val lastName: String?,
    @Json(name = "mobile_id") val mobileId: Int?,
    @Json(name = "profilepic") val profilePic: String?,
    @Json(name = "role_id") val roleId: Int?,
    @Json(name = "parent_id") val parentId: Int?,
    @Json(name = "isverify") val isVerify: Boolean?,
    @Json(name = "isfirsttime") val isFirstTime: Boolean?,
    @Json val city: City?,
    @Json val email: String?,
)

data class City(
    @Json (name = "id") val cityId: Int?,
    @Json (name = "title") val cityTitle: String?,
    @Json val province: Province?
)

data class Province(
    @Json (name = "id") val provinceId: Int?,
    @Json (name = "title") val provinceTitle: String?
)
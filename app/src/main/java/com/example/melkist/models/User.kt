package com.example.melkist.models

import com.squareup.moshi.Json

data class User(
    @Json val id: Int?,
    @Json (name = "name") val firstName: String?,
    @Json (name = "family") val lastName: String?,
    @Json (name = "mobile_id") val mobileId: Int?,
    @Json (name = "email") val email: String?,
    @Json (name = "profilepic") val profilePic: String?,
    @Json (name = "role_id") val roleId: Int?,
    @Json (name = "parent_id") val parentId: Int?,
    @Json (name = "isfirsttime") val isFirstTime: Boolean?,
    @Json val cityId: Int?,
    @Json val cityTitle: String?,
    @Json val provinceId: Int?,
    @Json val provinceTitle: String?,
    @Json (name = "realstate") val realEstate: String?, //TODO:
    @Json val token: String?
)

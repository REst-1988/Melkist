package com.example.melkist.models

import com.squareup.moshi.Json

data class User(
    @Json val id: Int?,
    @Json val firstName: String?, // firstName
    @Json val lastName: String?, // lastName
    @Json val mobileId: Int?,
    @Json val email: String?,
    @Json (name = "profilepic") val profilePic: String?,
    @Json val roleId: Int?,
    @Json val parentId: Int?,
    @Json val isFirstTime: Boolean?,
    @Json val cityId: Int?,
    @Json val cityTitle: String?,
    @Json val provinceId: Int?,
    @Json val provinceTitle: String?,
    @Json (name = "realstate") val realEstate: String?, //TODO:
    @Json val token: String?
)

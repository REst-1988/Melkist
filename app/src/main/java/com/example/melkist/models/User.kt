package com.example.melkist.models

import com.squareup.moshi.Json

data class User(
    @Json val id: Int?,
    @Json val firstName: String?, // firstName
    @Json val lastName: String?, // lastName
    val mobileId: Int?,
    val email: String?,
    val profilePic: String?,
    val roleId: Int?,
    val parentId: Int?,
    val isFirstTime: Boolean?,
    val cityId: Int?,
    val cityTitle: String?,
    val provinceId: Int?,
    val provinceTitle: String?,
    val token: String?
)

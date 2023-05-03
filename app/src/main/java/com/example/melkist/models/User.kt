package com.example.melkist.models

import com.squareup.moshi.Json

data class User(
    @Json val id: Int,
    @Json val name: String, // firstName
    @Json val family: String, // lastName
    @Json val mobileId: Int?,
    @Json val nationalCode: Long?,
    @Json val email: String?,
    @Json val password: String?,
    @Json val profilePic: String?,
    @Json val roleId: Int?,
    @Json val parentId: Int?,
    @Json val isVerify: Boolean?,
    @Json val createdAt: Long?,
    @Json val updatedAt: Long?,
    @Json val deletedAt: Long?
    // TODO: USER CITY WILL ADDED
)

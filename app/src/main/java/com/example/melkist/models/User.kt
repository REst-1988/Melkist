package com.example.melkist.models

data class User(
    val id: Int?,
    val firstName: String?, // firstName
    val lastName: String?, // lastName
    val mobileId: Int?,
    val mobileNo: String?,
    val email: String?,
    val password: String?,
    val profilePic: String?,
    val roleId: Int?,
    val parentId: Int?,
    val isVerify: Boolean?,
    val cityId: Int?,
    val cityTitle: String?,
    val provinceId: Int?,
    val provinceTitle: String?,
    val token: String?
)

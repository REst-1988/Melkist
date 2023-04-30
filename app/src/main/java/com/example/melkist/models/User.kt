package com.example.melkist.models

data class User(
    val id: Int?,
    val name: String, // firstName
    val family: String, // lastName
    val mobileId: Int,
    val nationalCode: Long,
    val email: String,
    val password: String,
    val profilePic: String?,
    val roleId: Int,
    val parentId: Int?,
    val isVerify: Boolean,
    val createdAt: Long?,
    val updatedAt: Long?,
    val deletedAt: Long?
    // TODO: USER CITY WILL ADDED
)

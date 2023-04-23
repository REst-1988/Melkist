package com.example.melkist.models

data class RealEstate(
    val id: Int,
    val title: String,
    val cityId: Int,
    val userId: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long
)

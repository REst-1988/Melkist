package com.example.melkist.models

data class Files(
    val id: Int,
    val userId: Int,
    val cityId: Int,
    val regions: String,
    val lat: Long,
    val lng: Int,
    val descriptions: String,
    val isShowExactAddress: Boolean,
    val requestType: Int,
    val fileCategories: String,
    val extraInfos: String,
    val createdAt: Long,
    val updatedAt: Long
)

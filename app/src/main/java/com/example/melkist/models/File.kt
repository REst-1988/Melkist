package com.example.melkist.models

data class File(
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

data class FileType(
    val id: Int,
    val title: String
)

data class FileTypes(
    val seeker: FileType = FileType(id = 1, title = "خواهان"),
    val owner: FileType = FileType(id = 2, title = "مالک")
)

package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.Serializable

data class FilterFileData(
    @Json (name = "sleepsNumber") val rooms: Period,
    @Json val price: Period,
    @Json val age: Period,
    @Json (name = "meterage") val size: Period,
    @Json (name = "") val typeId: Int,
    @Json (name = "") val catId: Int,
    @Json (name = "") val subCatId: Int,
    @Json (name = "region_id") val regionId: Int
): Serializable

data class FileType(
    val id: Int,
    val title: String
)

data class FileTypes(
    val seeker: FileType = FileType(id = 1, title = "خواهان"),
    val owner: FileType = FileType(id = 2, title = "مالک")
)

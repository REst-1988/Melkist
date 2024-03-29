package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.Serializable

data class FilterFileData(
    @Json (name = "user_id") var userId: Int?,
    @Json (name = "sleepsNumber") val rooms: Period,
    @Json (name = "price") val price: Period,
    @Json (name = "age") val age: Period,
    @Json (name = "meterage") val size: Period,
    @Json (name = "mortgage") val mortgage: Period,
    @Json (name = "rent") val rent: Period,
    @Json (name = "fileType_id") val typeId: Int?,
    @Json (name = "fileCategory_id") val catId: Int?,
    @Json (name = "fileCategoryType_id") val subCatId: Int?,
    @Json (name = "region_id") val regionId: Int?
): Serializable

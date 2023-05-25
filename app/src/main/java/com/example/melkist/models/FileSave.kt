package com.example.melkist.models

import com.squareup.moshi.Json

data class FileSave(
    @Json(name = "fileType_id") val fileTypeId: Int,
    @Json(name = "fileCategory_id") val fileCategoryId: Int,
    @Json(name = "fileCategoryType_id") val fileCategoryTypeId: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "city_id") val cityId: Int,
    @Json(name = "locations") val locations: List<Loc>,
    @Json val isShowExactAddress: Boolean,
    @Json val meterage: Period,
    @Json (name = "sleepsNumber") val rooms: Period,
    @Json val price: Period,
    @Json val description: String?,
    @Json val images: List<String?>?
)

data class Loc(
    @Json(name = "region_id") val regionId: Int?,
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lng") val lng: Double?,
)

data class Period(
    @Json val from: Long,
    @Json val to: Long
)
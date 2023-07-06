package com.example.melkist.models

import com.squareup.moshi.Json


data class Loc(
    @Json(name = "region_id") var regionId: Int? = 0,
    @Json(name = "lat") var lat: Double? = 0.0,
    @Json(name = "lng") var lng: Double? = 0.0,
)

data class Period(
    @Json val from: Long?,
    @Json val to: Long?
)

data class City(
    @Json (name = "id") val cityId: Int?,
    @Json (name = "title") val cityTitle: String?,
    @Json val province: Province?
)

data class Province(
    @Json (name = "id") val provinceId: Int?,
    @Json (name = "title") val provinceTitle: String?
)
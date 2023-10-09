package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.Serializable

data class RegionResponseModel(
    @Json val result: Boolean,
    @Json val data: List<RegionResponseData>
)

data class RegionResponseData(
    @Json val id: Int? = 0,
    @Json val title: String = "",
    @Json val tags: List<String>? = listOf(),
    @Json val lat: Double?,
    @Json val lng: Double?
): Serializable

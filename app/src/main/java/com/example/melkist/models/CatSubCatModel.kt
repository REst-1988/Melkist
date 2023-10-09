package com.example.melkist.models

import com.squareup.moshi.Json

data class CatSubCatResponse(
    @Json val result: Boolean?,
    @Json val data: List<CatSubCatModel>?,
    @Json val errors: List<String>?,
)

data class CatSubCatModel(
    @Json val id: Int,
    @Json val title: String,
    @Json(name = "dayiconurl") val dayIconUrl: String,
    @Json(name = "nighticonurl") val nightIconUrl: String
)

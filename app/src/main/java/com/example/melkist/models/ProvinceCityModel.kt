package com.example.melkist.models

import com.squareup.moshi.Json

data class ProvinceCityModel(
    @Json var result: Boolean?,
    @Json var data: List<Data>
)


data class Data(
    @Json var id: Int?,
    @Json var title: String?
)
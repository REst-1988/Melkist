package com.example.melkist.models

import com.squareup.moshi.Json

data class PcrsModel(
    @Json var result: Boolean? = null,
    @Json var data: List<PcrsData>? = null,
    @Json var errors: List<String>? = null
)

data class PcrsData(
    @Json var id: Int? = 0,
    @Json var title: String? = "",
    @Json (name = "province_id") var provinceId: Int? = null,
    @Json var user: User? = null
)

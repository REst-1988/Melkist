package com.example.melkist.models

import com.squareup.moshi.*

data class PublicResponseModel(
    @Json var result: Boolean?,
    @Json var message: String? = "",
    @Json var errors: List<String> = listOf()
)
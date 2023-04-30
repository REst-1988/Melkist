package com.example.melkist.models

import com.squareup.moshi.*

data class VerificationResponseModel(
    @Json var result: Boolean?,
    @Json var message: String? = "",
    @Json var errors: List<String> = listOf()
)
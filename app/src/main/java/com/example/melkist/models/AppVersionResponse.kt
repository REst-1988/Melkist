package com.example.melkist.models

import com.squareup.moshi.Json

data class AppVersionResponse(
    @Json val result: Boolean?,
    @Json (name = "resultFireBaseToken") val firebaseTokenResult: Boolean?,
    @Json (name = "resultVersionControll") val versionResult: Boolean?,
    @Json val isFirstTime: Boolean?,
    @Json val errors: List<String> = listOf()
)
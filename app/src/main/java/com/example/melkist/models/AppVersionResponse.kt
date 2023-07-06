package com.example.melkist.models

import com.squareup.moshi.Json

data class AppVersionResponse(
    @Json (name = "resultFireBaseToken") val firebaseTokenResult: Boolean?,
    @Json (name = "resultVersionControll") val versionResult: Boolean?
)
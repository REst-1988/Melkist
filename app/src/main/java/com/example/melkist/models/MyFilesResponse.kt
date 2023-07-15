package com.example.melkist.models

import com.squareup.moshi.Json

data class MyFilesResponse(
    @Json
    val result: Boolean?,
    @Json
    val data: List<FileData>?,
    @Json
    val errors: List<String>?
)
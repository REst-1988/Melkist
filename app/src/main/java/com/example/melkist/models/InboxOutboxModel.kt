package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.File

data class InboxOutboxModel(
    @Json val result: Boolean?,
    @Json val data: List<Status>?,
    @Json val errors: List<String>?
)

data class Status(
    @Json (name = "filerequest_id") val requestId: Int?,
    @Json val status: Int?, // 0, 1, null (Tiny Int) shows if request has been approved
    @Json val file: InboxOutBoxFile?,
    @Json val targetUser: User?
)

data class InboxOutBoxFile(
    @Json val id: Int?,
    @Json val price: Period?,
    @Json(name = "sleepsnumber") val rooms: Period?,
    @Json(name = "meterage") val size: Period?,
    @Json val locations: List<Location>?,
    @Json val images: List<String>?,
    @Json(name = "filetype_id") val typeId: Int?
)


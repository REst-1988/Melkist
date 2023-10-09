package com.example.melkist.models

import com.squareup.moshi.Json

data class FavFileResponse(
    @Json val result: Boolean?,
    @Json (name = "favorite_files") val data: List<Fav>?,
    @Json val errors: List<String>?
)

data class Fav(
    @Json val id: Int,
    @Json val locations: List<Location>,
    @Json (name = "meterage") val size: Period,
    @Json (name = "sleepsnumber") val roomNo: Period,
    @Json val price: Period,
    @Json (name = "images") val image: String?,
    @Json val user: User
)
package com.example.melkist.models

import com.squareup.moshi.Json

data class FavFileResponse(
    @Json val result: Boolean,
    @Json val data: List<Fav>
)

data class Fav(
    @Json val id: Int,
    @Json val locations: List<Location>,
    @Json (name = "meterage") val size: Period,
    @Json (name = "sleepsnumber") val roomNo: Period,
    @Json val price: Period,
    @Json val user: User
)
package com.example.melkist.models

import androidx.annotation.DrawableRes
import com.example.melkist.R
import com.squareup.moshi.Json

data class Role(
    @Json val id: Int,
    @Json val title: String,
    @Json val value: String?,
    @DrawableRes val avatar: Int?
)

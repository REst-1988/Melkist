package com.example.melkist.models

import androidx.annotation.DrawableRes
import com.example.melkist.R

data class Role(
    val id: Int,
    val title: String,
    val value: String,
    @DrawableRes val avatar: Int?
)

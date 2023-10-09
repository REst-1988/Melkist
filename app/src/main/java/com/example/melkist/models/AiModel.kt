package com.example.melkist.models

import com.squareup.moshi.Json

data class SuggestionModel (
    @Json val result: Boolean?,
    @Json val data: List<SuggestionData>?,
    @Json val errors: List<String> = listOf()
)

data class SuggestionData(
    @Json val myFile: FileData?,
    @Json val similarFiles: List<FileData>?
)

data class SuggestionItemListModel(
    val myFile: FileData?,
    val similarFile: FileData?
)


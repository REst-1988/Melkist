package com.example.melkist.models

import com.squareup.moshi.Json

data class Charts(
    @Json val result: Boolean?,
    @Json val data: List<ChartData>?,
    @Json val errors: List<String>?
)

data class ChartData(
    @Json val date: String?,
    @Json val sum: Double?
)

data class MemberImproData(
    var userId: Int,
    var userName: String,
    var chartData: List<ChartData>
)
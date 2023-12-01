package com.example.melkist.models

import com.squareup.moshi.Json

data class Action(
    @Json val id: Int?,
    @Json (name = "title") val actionTitle: String?,
    @Json var actionDate: Long? = null,
    @Json var actionOwnerName: String? = null,
    @Json var actionOwnerMobile: String? = null
)

data class Actions(
    @Json val actionSave: Action = Action(1, "ثبت"),
    @Json val actionVisit: Action = Action(2, "بازدید"),
    @Json val actionMeeting: Action = Action(3, "جلسه"),
    @Json val actionContract: Action = Action(4, "قرارداد"),
    @Json val actionConsult: Action = Action(5, "خاتمه")
)

data class FileActions(
    @Json val result: Boolean?,
    @Json val data: List<FileActionsData>?,
    @Json val errors: List<String>?
)

data class FileActionsData(
    @Json val performerUser: User?,
    @Json val action: Action?,
)
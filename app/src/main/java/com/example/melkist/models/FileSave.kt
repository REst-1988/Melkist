package com.example.melkist.models

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.squareup.moshi.Json
import org.json.JSONArray
import org.json.JSONObject

data class LocationResponse(
    @Json var result: Boolean?,
    @Json var data: List<LocationData>,
)

data class LocationData(
    @Json var id: Int?,
    @Json val locations: List<Loc>?
): ClusterItem {
    override fun getPosition(): LatLng = LatLng(locations!![0].lat!!, locations!![0].lng!!)

    override fun getTitle(): String = id.toString()

    override fun getSnippet(): String = id.toString()
}

data class FileResponse(
    @Json var result: Boolean? = null,
    @Json var data: List<FileSave>? = null,
    @Json var errors: List<String>? = null
)

data class FileSave(
    @Json(name = "fileType_id") val fileTypeId: Int,
    @Json(name = "fileCategory_id") val fileCategoryId: Int,
    @Json(name = "fileCategoryType_id") val fileCategoryTypeId: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "city_id") val cityId: Int,
    @Json(name = "locations") val locations: List<Loc>, // list because user can choose multi places
    @Json val isShowExactAddress: Boolean,
    @Json val meterage: Period,
    @Json(name = "sleepsNumber") val rooms: Period,
    @Json val price: Period,
    @Json val description: String?,
    @Json val images: List<String?>?
)

data class Loc(
    @Json(name = "region_id") var regionId: Int? = 0,
    @Json(name = "lat") var lat: Double? = 0.0,
    @Json(name = "lng") var lng: Double? = 0.0,
)

data class Period(
    @Json val from: Long,
    @Json val to: Long
)
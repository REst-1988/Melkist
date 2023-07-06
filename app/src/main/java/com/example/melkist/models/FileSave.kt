package com.example.melkist.models

import android.content.Context
import android.graphics.Color
import com.example.melkist.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.squareup.moshi.Json
data class LocationResponse(
    @Json var result: Boolean?,
    @Json var data: List<LocationData>,
    @Json val errors: List<String>?
)

data class LocationData(
    @Json var id: Int?,
    @Json val locations: List<Loc>?,
    @Json (name = "filetype") val fileTypeId: Int
): ClusterItem {
    override fun getPosition(): LatLng = LatLng(locations!![0].lat!!, locations[0].lng!!)

    override fun getTitle(): String = id.toString()

    override fun getSnippet(): String = id.toString()

    fun getLocColor (context: Context): BitmapDescriptor{
        val hsvBlue = FloatArray(3)
        Color.colorToHSV(context.resources.getColor(R.color.main_dark_color2), hsvBlue)
        val hsvRed = FloatArray(3)
        Color.colorToHSV(context.resources.getColor(R.color.main_green_color2), hsvRed)
        return if (fileTypeId == FileTypes().owner.id)
            BitmapDescriptorFactory.defaultMarker(hsvBlue[0])
        else
            BitmapDescriptorFactory.defaultMarker(hsvRed[0])
    }
}

data class FileResponse(
    @Json var result: Boolean? = null,
    @Json var data: List<FileSave>? = null,
    @Json var errors: List<String>? = null
)

data class FileSave(
    @Json(name = "fileType_id") val typeId: Int,
    @Json(name = "fileCategory_id") val catId: Int,
    @Json(name = "fileCategoryType_id") val subCatId: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "city_id") val cityId: Int,
    @Json(name = "locations") val locations: List<Loc>, // list because user can choose multi places
    @Json val isShowExactAddress: Boolean,
    @Json (name = "meterage") val size: Period,
    @Json (name = "sleepsNumber") val rooms: Period,
    @Json val age: Period,
    @Json val price: Period,
    @Json val description: String?,
    @Json val images: List<String?>?
)
package com.example.melkist.models

import com.squareup.moshi.Json
import java.io.Serializable

data class FileDataResponse(
    @Json val result: Boolean?,
    @Json val data: FileData?,
    @Json val errors: List<String>?
) : Serializable

data class FileData(
    @Json val id: Int,
    @Json val locations: List<Location>,
    @Json val description: String?,
    @Json(name = "isshowexactaddress") val isShowExactAddress: Boolean?,
    @Json(name = "meterage") val size: Period?,
    @Json(name = "sleepsnumber") val roomNo: Period?,
    @Json val price: Period?,
    @Json val age: Period?,
    @Json(name = "created_at") val created_at: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    @Json val images: List<String>?,
    @Json var user: User?,
    @Json val city: City?,
    @Json(name = "filetypefilecategoryfilecategorytype") val typeInfo: TypeInfo?,
    @Json var isFav: Boolean?,
    @Json val status: Int?,
    @Json val mobileNumber: String?,
    @Json (name = "ownermobile_id") val ownerMobileId: String?,
    @Json val mortgage: Period?,
    @Json val rent: Period?,
    @Json val suitablefor: Int?,
    @Json val floor: Int?,
    @Json val parking: Boolean?,
    @Json val storeRoom: Boolean?,
    @Json (name = "balacon") val balcony: Boolean?,
    @Json val elevator: Boolean?,
    @Json (name = "administrativedeed") val adminDeed: Boolean?,
    @Json val deedType: Int?
): Serializable

data class Location(
    @Json val region: RegionResponseData,
    @Json val lat: String,
    @Json val lng: String
): Serializable

data class TypeInfo(
    @Json(name = "filetype") val fileType: IdTitle?,
    @Json(name = "filecategory") val category: IdTitle?,
    @Json(name = "filecategorytype") val subCategory: IdTitle?
): Serializable

data class IdTitle(
    @Json val id: Int?,
    @Json val title: String
): Serializable
package com.example.melkist.viewmodels

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.FileData
import com.example.melkist.models.FileSave
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Loc
import com.example.melkist.models.Period
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.User
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.CITY
import com.example.melkist.utils.PROVINCE
import com.example.melkist.utils.REGION_1
import com.example.melkist.utils.REGION_2
import com.example.melkist.utils.REGION_3
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.net.URL


class AddItemViewModel : ViewModel() {

    var extra: Serializable? = null
    enum class ItemType { CHOOSE, SEEKER, OWNER }
    enum class ReqSource { CATEGORY, SUB_CATEGORY }
    enum class Cr { PROVINCE, CITY, REGION_1, REGION_2, REGION_3 }

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status

    private val _saveResponse = MutableLiveData<PublicResponseModel>()
    val saveResponse: LiveData<PublicResponseModel> = _saveResponse

    private var itemType: ItemType = ItemType.CHOOSE
    fun getItemType() = itemType
    fun setItemType(type: ItemType) {
        itemType = type
    }

    private var crCondition: Cr = Cr.CITY
    fun getLocReqSource() = crCondition
    fun setLocReqSource(crCondition: Cr) {
        this.crCondition = crCondition
    }

    private var typeId: Int = 0

    var catId: Int = 0
    var catTitle: String = ""
    var subCatId: Int = 0
    var subCatTitle: String = ""

    var provinceId: Int = 0
    var provinceTitle: String = ""
    var cityId: Int = 0
    var cityTitle: String = ""
    var regionId: Int = 0
    var regionTitle: String = ""
    var regionLat: Double? = null
    var regionLng: Double? = null
    var region2Id: Int? = null
    var region2Title: String? = null
    var region3Id: Int? = null
    var region3Title: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var isShowExactAddress: Boolean = true
    var ageFrom: Int? = null
    var ageTo: Int? = null
    var sizeFrom: Int? = null
    var sizeTo: Int? = null
    var roomFrom: Int? = null
    var roomTo: Int? = null
    var totalPriceFrom: Long? = null
    var totalPriceTo: Long? = null
    var descriptions: String? = null

    var mortgageFrom: Long? = null
    var mortgageTo: Long? = null
    var rentFrom: Long? = null
    var rentTo: Long? = null
    var suitableForText: String? = null // family , family and bachelor
    var suitableFor: Int? = null // family , family and bachelor
    var floorFromText: String? = null
    var floorFrom: Int? = null
    var floorTo: Int? = null
    var parking: Boolean? = null
    var storeRoom: Boolean? = null
    var balcony: Boolean? = null
    var elevator: Boolean? = null
    var administrativeDeed: Boolean? = null
    var deedTypeText: String? = null
    var deedType: Int? = null

    private var _mapSnapShot = MutableLiveData<Bitmap>()
    val mapSnapShot: LiveData<Bitmap> = _mapSnapShot
    fun setMapSnapShot(bitmap: Bitmap) {
        _mapSnapShot.value = bitmap
    }

    val bitmapList = arrayListOf<Bitmap?>()
    private val listOfEncodedImages: MutableList<String?> = mutableListOf()
    var listOfImageUrls: List<String?>? = null // when editing
    lateinit var fileSave: FileSave

    fun getTypeId(): Int {
        val fileTypes = FileTypes()
        typeId = if (getItemType() == ItemType.SEEKER) fileTypes.seeker.id
        else if (getItemType() == ItemType.OWNER) fileTypes.owner.id
        else -1
        return typeId
    }

    private fun gatheringData(userId: Int) {
        readyImagesForSend()
        logSendData(userId) // TODO: no need for this this is just for checking

        fileSave = FileSave(
            typeId = typeId,
            catId = catId,
            subCatId = subCatId,
            userId = userId,
            cityId = cityId,
            locations = listOf(
                Loc(regionId, lat, lng), Loc(region2Id, null, null), Loc(region3Id, null, null)
            ),
            isShowExactAddress = isShowExactAddress,
            size = Period(
                sizeFrom?.toLong(), getValueFrom(sizeFrom?.toLong(), sizeTo?.toLong())
            ),
            rooms = Period(
                roomFrom?.toLong(), getValueFrom(roomFrom?.toLong(), roomTo?.toLong())
            ),
            age = Period(
                ageFrom?.toLong(), getValueFrom(ageFrom?.toLong(), ageTo?.toLong())
            ),
            price = Period(
                totalPriceFrom, getValueFrom(totalPriceFrom, totalPriceTo)
            ),
            description = descriptions,
            images = listOfEncodedImages
        )
    }

    fun splitData(file: FileData) {
        Log.e("TAG", "splitData: $file\n\n\n")
        file.typeInfo?.apply {
            typeId = fileType?.id ?: 0
            catId = category?.id ?: 0
            catTitle = category?.title ?: "null"
            subCatId = subCategory?.id ?: 0
            subCatTitle = subCategory?.title ?: "null"
        }
        setItemType(
            if (typeId == FileTypes().owner.id) ItemType.OWNER
            else ItemType.SEEKER
        )
        file.city?.let { city ->
            provinceId = city.province?.provinceId ?: 0
            provinceTitle = city.province?.provinceTitle ?: "null"
            cityId = city.cityId ?: 0
            cityTitle = city.cityTitle ?: "null"
        }
        regionId = file.locations[0].region.id ?: 0
        regionTitle = file.locations[0].region.title
        regionLat = file.locations[0].region.lat
        regionLng = file.locations[0].region.lng
        try {
            region2Id = file.locations[1].region.id
            region2Title = file.locations[1].region.title
            region3Id = file.locations[2].region.id
            region3Title = file.locations[2].region.title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lat = file.locations[0].region.lat
        lng = file.locations[0].region.lng
        //isShowExactAddress =
        ageFrom = file.age.from?.toInt()
        ageTo = file.age.to?.toInt()
        sizeFrom = file.size.from?.toInt()
        sizeTo = file.size.to?.toInt()
        roomFrom = file.roomNo.from?.toInt()
        roomTo = file.roomNo.to?.toInt()
        totalPriceFrom = file.price.from
        totalPriceTo = file.price.to
        descriptions = file.description
        listOfImageUrls = file.images
//
        /*   TODO:     mortgageFrom =
                mortgageTo =
                rentFrom =
                rentTo =
                suitableForText =  // family , family and bachelor
                suitableFor =  // family , family and bachelor
                floorFromText =
                floorFrom =
                floorTo =
                parking =
                storeRoom =
                balcony =
                elevator =
                administrativeDeed =
                deedTypeText =
                deedType =*/

        logSendData(1)
    }

    private fun logSendData(userId: Int) {// TODO: delete this after testing
        Log.e(
            "TAG",
            "gatheringData: " +
                    "            typeId = $typeId,\n" +
                    "            catId = $catId,\n" +
                    "            catTitle = $catTitle,\n" +
                    "            subCatId = $subCatId,\n" +
                    "            subCatTitle = $subCatTitle,\n" +
                    "            provinceId = $provinceId,\n" +
                    "            provinceTitle = $provinceTitle,\n" +
                    "            cityId = $cityId,\n" +
                    "            cityTitle = $cityTitle,\n" +
                    "            userId = $userId,\n" +
                    "            locations = listOf(Loc($regionId, $lat, $lng),Loc($region2Id, null, null),Loc($region3Id, null, null)),\n" +
                    "            isShowExactAddress = $isShowExactAddress,\n" +
                    "            size = Period(${sizeFrom?.toLong()}, (${(sizeTo ?: sizeFrom)?.toLong()}),\n" +
                    "            rooms = Period(${roomFrom?.toLong()}, ${roomTo?.toLong()}),\n" +
                    "            age = Period(${ageFrom?.toLong()}, ${ageTo?.toLong()}),\n" +
                    "            price = Period($totalPriceFrom, ${totalPriceTo}),\n" +
                    "            description = $descriptions,\n" +

                    "            mortgageFrom = $mortgageFrom,\n" +
                    "            mortgageTo = $mortgageTo,\n" +
                    "            rentFrom = $rentFrom,\n" +
                    "            rentTo = $rentTo,\n" +
                    "            suitableForText = $suitableForText,\n" +
                    "            suitableFor = $suitableFor,\n" +
                    "            floorFromText = $floorFromText,\n" +
                    "            floorFrom = $floorFrom,\n" +
                    "            floorTo = $floorTo,\n" +
                    "            parking = $parking,\n" +
                    "            storeRoom = $storeRoom,\n" +
                    "            balcony = $balcony,\n" +
                    "            elevator = $elevator,\n" +
                    "            administrativeDeed = $administrativeDeed,\n" +
                    "            deedTypeText = $deedTypeText,\n" +
                    "            deedType = $deedType,\n" +
                    "            images = listOfImages",
        )
    }

    private fun readyImagesForSend() {
        Log.e("TAG", "readyImagesForSend: ${bitmapList.size}", )
        bitmapList.forEach { img ->
            var encodedImageMain: String? = null
            img?.apply {
                val main = ThumbnailUtils.extractThumbnail(this, 594, 400)
                val byteArrayOutputStreamMain = ByteArrayOutputStream()
                main.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamMain)
                encodedImageMain =
                    Base64.encodeToString(byteArrayOutputStreamMain.toByteArray(), Base64.DEFAULT)
            }
            listOfEncodedImages.add(encodedImageMain)
        }
    }

    private fun isOwner() = typeId == FileTypes().owner.id

    private fun getValueFrom(valueFrom: Long?, valueTo: Long?): Long? {
        return if (isOwner()) valueFrom
        else valueTo
    }

    fun saveFile(activity: Activity, user: User?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            saveFile(activity, user)
        }
        else {
            user?.apply {
                gatheringData(id!!)
            }
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _saveResponse.value = Api.retrofitService.saveFile(user?.token, fileSave)
                    Log.e("TAG", "saveFile: ${_saveResponse.value}", )
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(
                        viewModelScope,
                        "${user?.id}, ${this@AddItemViewModel.javaClass.name}, getRegion, ",
                        e
                    )
                }
            }
        }
    }

    fun resetAddItemFieldsByChoosingType() {
        catId = 0
        catTitle = ""
        subCatId = 0
        subCatTitle = ""
    }

    fun resetAddItemFieldsByChoosingCategory() {
        subCatId = 0
        subCatTitle = ""
    }

    fun resetAddLocationFieldsByProvince() {
        cityId = 0
        cityTitle = ""
        regionId = 0
        regionTitle = ""
        lat = null
        lng = null
        isShowExactAddress = false
        region2Id = null
        region2Title = null
        region3Id = null
        region3Title = null
    }

    fun resetAddLocationFieldsByCity() {
        regionId = 0
        regionTitle = ""
        lat = null
        lng = null
        isShowExactAddress = false
        region2Id = null
        region2Title = null
        region3Id = null
        region3Title = null
    }

    fun resetAddLocationFieldsByRegion1() {
        lat = null
        lng = null
        isShowExactAddress = false
    }

    fun isReadyForChooseRegion(): Boolean {
        return cityId != 0
    }

    fun getLocReqSourceKey(): Int {
        return when (getLocReqSource()) {
            Cr.PROVINCE -> PROVINCE
            Cr.CITY -> CITY
            Cr.REGION_1 -> REGION_1
            Cr.REGION_2 -> REGION_2
            Cr.REGION_3 -> REGION_3
        }
    }

    //////////////////////////////////////////////////////////////
    fun isShowAgeField(): Boolean {
        val result = com.example.melkist.utils.isShowAgeField(catId, subCatId)
        if (!result) {
            ageFrom = null
            ageTo = null
        }
        return result
    }

    fun isShowSizeField(): Boolean {
        val result = com.example.melkist.utils.isShowSizeField(catId, subCatId)
        if (!result) {
            sizeFrom = null
            sizeTo = null
        }
        return result
    }

    fun isShowRoomsField(): Boolean {
        val result = com.example.melkist.utils.isShowRoomsField(catId, subCatId)
        if (!result) {
            roomFrom = null
            roomTo = null
        }
        return result
    }

    fun isShowTotalPriceField(): Boolean {
        val result = com.example.melkist.utils.isShowTotalPriceField(catId, subCatId)
        if (!result) {
            totalPriceFrom = null
            totalPriceTo = null
        }
        return result
    }

    fun isShowMortgageField(): Boolean {
        val result = com.example.melkist.utils.isShowMortgageField(catId, subCatId)
        if (!result) {
            mortgageFrom = null
            mortgageTo = null
        }
        return result
    }

    fun isShowRentField(): Boolean {
        val result = com.example.melkist.utils.isShowRentField(catId, subCatId)
        if (!result) {
            rentFrom = null
            rentTo = null
        }
        return result
    }

    fun isShowSuitableForField(): Boolean {
        val result = com.example.melkist.utils.isShowSuitableForField(catId, subCatId)
        if (!result) {
            suitableFor = null
            suitableForText = null
        }
        return result
    }

    fun isShowFloorField(): Boolean {
        val result = com.example.melkist.utils.isShowFloorField(catId, subCatId)
        if (!result) {
            floorFrom = null
            floorTo = null
            floorFromText = null
        }
        return result
    }

    fun isShowParkingField(): Boolean {
        val result = com.example.melkist.utils.isShowParkingField(catId, subCatId)
        if (!result) {
            parking = null
        }
        return result
    }

    fun isShowStoreRoomField(): Boolean {
        val result = com.example.melkist.utils.isShowStoreRoomField(catId, subCatId)
        if (!result) {
            storeRoom = null
        }
        return result
    }

    fun isShowBalconyField(): Boolean {
        val result = com.example.melkist.utils.isShowBalconyField(catId, subCatId)
        if (!result) {
            balcony = null
        }
        return result
    }

    fun isShowElevatorField(): Boolean {
        val result = com.example.melkist.utils.isShowElevatorField(catId, subCatId)
        if (!result) {
            elevator = null
        }
        return result
    }

    fun isShowAdminDeedField(): Boolean {
        val result = com.example.melkist.utils.isShowAdminDeedField(catId, subCatId)
        if (!result) {
            administrativeDeed = null
        }
        return result
    }

    fun isShowDeedTypeField(): Boolean {
        val result = com.example.melkist.utils.isShowDeedTypeField(catId, subCatId)
        if (!result) {
            deedTypeText = null
            deedType = null
        }
        return result
    }

    fun addImageUrlToBitmap(url: URL) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                bitmapList.add(bitmap)
                Log.e("TAG", "addImageUrlToBitmap: TEST ${bitmapList[0]}, ${BitmapFactory.decodeStream(url.openConnection().getInputStream())}")
            } catch (e: Exception) {
                handleSystemException(this, "AddItemViewModel, addImageUrlToBitmap", e)
            }
        }
    }
}






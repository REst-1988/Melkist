package com.example.melkist.viewmodels

import android.app.Activity
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.FileSave
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Loc
import com.example.melkist.models.Period
import com.example.melkist.models.PublicResponseModel
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
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddItemViewModel : ViewModel() {

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

    private var reqSource: ReqSource = ReqSource.CATEGORY
    fun getReqSource() = reqSource
    fun setReqSource(reqSource: ReqSource) {
        this.reqSource = reqSource
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
    var isShowExactAddress: Boolean = false
    var ageFrom: Int? = null
    var ageTo: Int? = null
    var sizeFrom: Int? = null
    var sizeTo: Int? = null
    var roomFrom: Int? = null
    var roomTo: Int? = null
    var priceFrom: Long? = null
    var priceTo: Long? = null
    var descriptions: String? = null

    private var _mapSnapShot = MutableLiveData<Bitmap>()
    val mapSnapShot: LiveData<Bitmap> = _mapSnapShot
    fun setMapSnapShot(bitmap: Bitmap) {
        _mapSnapShot.value = bitmap
    }

    var image1: Bitmap? = null
    var image2: Bitmap? = null
    var image3: Bitmap? = null
    var image4: Bitmap? = null
    var image5: Bitmap? = null
    var image6: Bitmap? = null
    private val listOfImages: MutableList<String?> = mutableListOf()
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
                priceFrom, getValueFrom(priceFrom, priceTo)
            ),
            description = descriptions,
            images = listOfImages
        )
    }

    private fun logSendData(userId: Int) {// TODO: delete this after testing
        Log.e(
            "TAG",
            "gatheringData: " + "            typeId = $typeId,\n" + "            catId = $catId,\n" + "            subCatId = $subCatId,\n" + "            userId = $userId,\n" + "            cityId = $cityId,\n" + "            locations = listOf(Loc($regionId, $lat, $lng),Loc($region2Id, null, null),Loc($region3Id, null, null)),\n" + "            isShowExactAddress = $isShowExactAddress,\n" + "            size = Period(${sizeFrom?.toLong()}, (${(sizeTo ?: sizeFrom)?.toLong()}),\n" + "            rooms = Period(${roomFrom?.toLong()}, ${roomTo?.toLong()}),\n" + "            age = Period(${ageFrom?.toLong()}, ${ageTo?.toLong()}),\n" + "            price = Period($priceFrom, ${priceTo}),\n" + "            description = $descriptions,\n" + "            images = listOfImages",
        )
    }

    private fun readyImagesForSend() {
        val list = arrayListOf(image1, image2, image3, image4, image5, image6)
        list.forEach { img ->
            var encodedImageMain: String? = null
            img?.apply {
                val main = ThumbnailUtils.extractThumbnail(this, 594, 400)
                val byteArrayOutputStreamMain = ByteArrayOutputStream()
                main.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamMain)
                encodedImageMain =
                    Base64.encodeToString(byteArrayOutputStreamMain.toByteArray(), Base64.DEFAULT)
            }
            listOfImages.add(encodedImageMain)
        }
    }

    private fun isOwner() = typeId == FileTypes().owner.id

    private fun getValueFrom(valueFrom: Long?, valueTo: Long?): Long? {
        return if (isOwner()) valueFrom
        else valueTo
    }

    fun saveFile(activity: Activity, userId: Int) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            saveFile(activity, userId)
        }
        else {
            gatheringData(userId)
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _saveResponse.value = Api.retrofitService.saveFile(fileSave)
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "${this@AddItemViewModel.javaClass.name}, getRegion, ", e)
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

    fun resetImages() {
        image1 = null
        image2 = null
        image3 = null
        image4 = null
        image5 = null
        image6 = null
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
}
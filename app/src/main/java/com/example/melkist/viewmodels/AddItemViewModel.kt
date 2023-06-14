package com.example.melkist.viewmodels

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.*
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddItemViewModel : ViewModel() {

    enum class ItemType { CHOOSE, SEEKER, OWNER }
    enum class ReqSource { CATEGORY, SUB_CATEGORY }
    enum class Cr {PROVINCE, CITY, REGION_1, REGION_2, REGION_3}

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
/*    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList*/
    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList

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

    var provinceId: Int = 17  //TODO: later it should read from cash a default value of user place
    var provinceTitle: String = "فارس" //TODO: later it should read from cash a default value of user place
    var cityId: Int = 733 //TODO: later it should read from cash a default value of user place
    var cityTitle: String = "شیراز" //TODO: later it should read from cash a default value of user place
    var regionId: Int = 0
    var regionTitle: String = ""// معالی‌آباد
    var region2Id: Int? = null
    var region2Title: String? = null
    var region3Id: Int? = null
    var region3Title: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var isShowExactAddress: Boolean = false
    var sizeFrom: Int = 0
    var sizeTo: Int? = null
    var rooms: Int = 0
    var priceFrom: Long = 0
    var priceTo: Long? = null
    var descriptions: String? = null

    var image1: Bitmap? = null
    var image2: Bitmap? = null
    var image3: Bitmap? = null
    var image4: Bitmap? = null
    var image5: Bitmap? = null
    var image6: Bitmap? = null
    private val listOfImages: MutableList<String?> = mutableListOf()

    lateinit var fileSave: FileSave


/*    fun getTypeId(): Int {
        val fileTypes = FileTypes()
        typeId = if (getItemType() == ItemType.SEEKER)
            fileTypes.seeker.id
        else if (getItemType() == ItemType.OWNER)
            fileTypes.owner.id
        else
            -1
        return typeId
    }*/

    fun getTypeTitle(): String {
        val fileTypes = FileTypes()
        return if (getItemType() == ItemType.SEEKER)
            fileTypes.seeker.title
        else if (getItemType() == ItemType.OWNER)
            fileTypes.owner.title
        else
            ""
    }

/*    fun getFileCategories() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _itemOptionList.value =
                    Api.retrofitService.getFileCategories(getTypeId()).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }*/

    fun choosingItemActionPc(pcrs: PcrsData){
        when (crCondition) {
            Cr.PROVINCE -> {
                provinceId = pcrs.id!!
                provinceTitle = pcrs.title!!
                resetAddLocationFieldsByProvince()
            }
            Cr.CITY -> {
                cityId = pcrs.id!!
                cityTitle = pcrs.title!!
                resetAddLocationFieldsByCity()
            }
            else -> {}
        }
    }

    fun choosingItemActionRegion(RegionRes: RegionResponseData){
        when (crCondition) {
            Cr.REGION_1 -> {
                regionId = RegionRes.id!!
                regionTitle = RegionRes.title
            }
            Cr.REGION_2 -> {
                region2Id = RegionRes.id!!
                region2Title = RegionRes.title
            }
            Cr.REGION_3 -> {
                region3Id = RegionRes.id!!
                region3Title = RegionRes.title
            }
            else -> {}
        }
    }

/*    fun getFileCategoryType() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e("TAG", "getFileCategoryType: ${getTypeId()} , $catId", )
                _itemOptionList.value =
                    Api.retrofitService.getFileCategoryTypes(getTypeId(), catId = catId).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }*/

    private fun gatheringData() {
        val list = arrayListOf(image1, image2, image3, image4, image5, image6)
        for (img in list){
            var encodedImageMain: String? = null
            if (img != null) {
                val main = ThumbnailUtils.extractThumbnail(img, 500, 337)
                val byteArrayOutputStreamMain = ByteArrayOutputStream()
                main.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamMain)
                encodedImageMain =
                    Base64.encodeToString(byteArrayOutputStreamMain.toByteArray(), Base64.DEFAULT)
            }
            listOfImages.add(encodedImageMain)
        }
        fileSave = FileSave(
            typeId,
            catId,
            subCatId,
            userId = 1,// TODO: this must read from user data
            cityId,
            listOf(Loc(regionId, lat, lng),Loc(region2Id, null, null),Loc(region3Id, null, null)),
            isShowExactAddress,
            Period(sizeFrom.toLong(), (sizeTo?:sizeFrom).toLong()),
            Period(rooms.toLong(), rooms.toLong()), // If later on need period it should be changed
            Period(priceFrom, priceTo?:priceFrom),
            descriptions,
            listOfImages
        )
    }

    fun saveFile () {
        gatheringData()
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _saveResponse.value =
                    Api.retrofitService.saveFile(fileSave)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

/*    fun emptyList() {
        _itemOptionList.value = listOf()
    }*/
/*    fun emptyPcList(){
        _pcrsList.value = listOf()
    }*/ //TODO: Check this if don't need delete

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
        region2Id = 0
        region2Title = ""
        region3Id = 0
        region3Title = ""
    }

    fun resetAddLocationFieldsByCity() {
        regionId = 0
        regionTitle = ""
        lat = null
        lng = null
        isShowExactAddress = false
        region2Id = 0
        region2Title = ""
        region3Id = 0
        region3Title = ""
    }

    fun resetAddLocationFieldsByRegion1() {
        regionId = 0
        regionTitle = ""
        lat = null
        lng = null
        isShowExactAddress = false
    }

    fun isReadyForChooseRegion(): Boolean{
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
}
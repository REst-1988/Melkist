package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.*
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.OWNER_ITEM_TYPE
import com.example.melkist.utils.SEEKER_ITEM_TYPE
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    enum class ItemType { SHOW_ALL, SHOW_SEEKER, SHOW_OWNER }
    enum class ReqSource { CATEGORY, SUB_CATEGORY }

    private var reqSource: AddItemViewModel.ReqSource = AddItemViewModel.ReqSource.CATEGORY
    fun getReqSource() = reqSource
    fun setReqSource(reqSource: AddItemViewModel.ReqSource) {
        this.reqSource = reqSource
    }

    private var itemType: ItemType = ItemType.SHOW_ALL
    fun getItemType() = itemType
    fun setItemType(type: ItemType) {
        itemType = type
    }

    fun getTypeId(requestCode: ItemType): Int {
        val fileTypes = FileTypes()
        return when (requestCode) {
            ItemType.SHOW_SEEKER -> fileTypes.seeker.id
            ItemType.SHOW_OWNER -> fileTypes.owner.id
            else -> -1
        }
    }

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status

    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList
    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList
    private val _regionList = MutableLiveData<List<RegionResponseData>>()
    val regionList: LiveData<List<RegionResponseData>> = _regionList

    private val _filesResponse = MutableLiveData <FileResponse>()
    val filesResponse: LiveData<FileResponse> = _filesResponse
    private val _locationResponse = MutableLiveData <LocationResponse>()
    val locationResponse: LiveData<LocationResponse> = _locationResponse

    private val _fileAllData = MutableLiveData<FileAllDataResponse>()
    val fileAllData: LiveData<FileAllDataResponse> = _fileAllData

    var catId: Int = 0
    var catTitle: String = ""
    var subCatId: Int = 0
    var subCatTitle: String = ""

    fun getFiles(token: String, cityId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _locationResponse.value =
                    Api.retrofitService.getAllFilesByCity(token = token, cityId = cityId)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getFileInfoById(token: String, fileId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _fileAllData.value =
                    Api.retrofitService.getFileInfoById(token, fileId)
                Log.e("TAG", "getFileInfoById: ${fileAllData.value!!.data}", )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun resetCatSubCat() {
        catId = 0
        catTitle = ""
        subCatId = 0
        subCatTitle = ""
    }
}
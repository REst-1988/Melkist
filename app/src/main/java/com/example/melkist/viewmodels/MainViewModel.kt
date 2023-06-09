package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.FavFileResponse
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.FileResponse
import com.example.melkist.models.FileTypes
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.InboxOutboxModel
import com.example.melkist.models.LocationResponse
import com.example.melkist.models.PcrsData
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.RegionResponseData
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.REGION_1
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {// TODO: every view model should combined to 4 view model  (splash  main  add login)

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _fileAllData = MutableLiveData<FileDataResponse>()
    val fileAllData: LiveData<FileDataResponse> = _fileAllData
    private val _saveFavResponse = MutableLiveData<PublicResponseModel>()
    val saveFavResponse: LiveData<PublicResponseModel> = _saveFavResponse
    private val _publicResponse = MutableLiveData<PublicResponseModel> ()
    val publicResponse: LiveData<PublicResponseModel> = _publicResponse
    private val _favList = MutableLiveData<FavFileResponse>()
    val favList: LiveData<FavFileResponse> = _favList
    private val _inboxResponse = MutableLiveData<InboxOutboxModel>()
    val inboxResponse: LiveData<InboxOutboxModel> = _inboxResponse
    private val _outboxResponse = MutableLiveData<InboxOutboxModel>()
    val outboxResponse: LiveData<InboxOutboxModel> = _outboxResponse

    enum class ItemType { SHOW_ALL, SHOW_SEEKER, SHOW_OWNER }
    enum class ReqSource { CATEGORY, SUB_CATEGORY }

    private var reqSource: AddItemViewModel.ReqSource = AddItemViewModel.ReqSource.CATEGORY
    fun getReqSource() = reqSource
    fun setReqSource(reqSource: AddItemViewModel.ReqSource) {
        this.reqSource = reqSource
    }

    private val _locationResponse = MutableLiveData <LocationResponse>()
    val locationResponse: LiveData<LocationResponse> = _locationResponse

    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList
    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList
    private val _regionList = MutableLiveData<List<RegionResponseData>>()
    val regionList: LiveData<List<RegionResponseData>> = _regionList

    private val _filesResponse = MutableLiveData <FileResponse>()
    val filesResponse: LiveData<FileResponse> = _filesResponse

    var catId: Int? = null
    var catTitle: String? = null
    var subCatId: Int? = null
    var subCatTitle: String? = null
    var regionId: Int? = null
    var regionTitle: String? = null
    private var itemType: ItemType = ItemType.SHOW_ALL
    fun getItemType() = itemType
    fun setItemType(type: ItemType) {
        itemType = type
    }

    fun getFiles(token: String, cityId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _locationResponse.value =
                    Api.retrofitService.getAllFilesByCity(token = token, cityId = cityId)
                Log.e("TAG", "getFileInfoById: ${_locationResponse.value!!.data}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }
    fun getFilterFiles(token: String, filterFileData: FilterFileData){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _locationResponse.value =
                    Api.retrofitService.filterFiles(token = token, filterFileData)
                Log.e("TAG", "getFileInfoById: ${_locationResponse.value!!.data}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun resetLocations() {
        locationResponse.value!!.data = listOf()
    }

    fun getTypeId(requestCode: ItemType): Int {
        val fileTypes = FileTypes()
        return when (requestCode) {
            ItemType.SHOW_SEEKER -> fileTypes.seeker.id
            ItemType.SHOW_OWNER -> fileTypes.owner.id
            else -> -1
        }
    }
    fun resetCatSubCat() {
        catId = null
        catTitle = null
        subCatId = null
        subCatTitle = null
    }

    fun resetSubCat() {
        subCatId = null
        subCatTitle = null
    }

    fun getReqSourceNumber(): Int {
        return REGION_1
    }

    fun getFileInfoById(token: String, fileId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _fileAllData.value =
                    Api.retrofitService.getFileInfoById(token, fileId)
                Log.e("TAG", "getFileInfoById: ${fileAllData.value!!.data}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun saveFavFile (token: String, userId: Int, fileId: Int) {
        viewModelScope.launch{
            _status.value = ApiStatus.LOADING
            try {
                _saveFavResponse.value =
                    Api.retrofitService.saveFavoriteFile(
                        token,
                        userId,
                        fileId
                    )
                Log.e("TAG", "getOutbox: ${_saveFavResponse.value.toString()} ", )
                _status.value = ApiStatus.DONE
            } catch (e: Exception){
                _status.value = ApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun sendCooperationRequest(
        token: String,
        userId: Int,
        fileId: Int
    ) {
        viewModelScope.launch{
            _status.value = ApiStatus.LOADING
            try {
                _publicResponse.value =
                    Api.retrofitService.sendCooperationRequest(
                        token,
                        userId,
                        fileId
                    )
                Log.e("TAG", "getOutbox: ${_publicResponse.value.toString()} ", )
                _status.value = ApiStatus.DONE
            } catch (e: Exception){
                _status.value = ApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun getFavoritesFile (token: String, userId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _favList.value = Api.retrofitService.getFavoriteFiles(token, userId)
                Log.e("TAG", "getOutbox: ${_favList.value.toString()} ", )
                _status.value = ApiStatus.DONE
            } catch (e: Exception){
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getInbox(userId: Int, token: String){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _inboxResponse.value =
                    Api.retrofitService.inbox(token = token, userId = userId)
                Log.e("TAG", "getOutbox: ${_inboxResponse.value.toString()} ", )
                _status.value = ApiStatus.DONE
            }catch (e: java.lang.Exception){
                _status.value = ApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun getOutbox(userId: Int, token: String){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _outboxResponse.value =
                    Api.retrofitService.outbox(token = token, userId = userId)
                Log.e("TAG", "getOutbox: ${_outboxResponse.value.toString()} ", )
                _status.value = ApiStatus.DONE
            }catch (e: java.lang.Exception){
                _status.value = ApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }
}
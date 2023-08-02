package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.FavFileResponse
import com.example.melkist.models.FileData
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.FileResponse
import com.example.melkist.models.FileTypes
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.InboxOutboxModel
import com.example.melkist.models.LocationResponse
import com.example.melkist.models.MyFilesResponse
import com.example.melkist.models.PcrsData
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.RegionResponseData
import com.example.melkist.models.User
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.REGION_1
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class MainViewModel :
    ViewModel() {// TODO: every view model should combined to 4 view model  (splash  main  add login)

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _fileAllData = MutableLiveData<FileDataResponse>()
    val fileAllData: LiveData<FileDataResponse> = _fileAllData
    private val _deleteFileResponse = MutableLiveData<PublicResponseModel>()
    val deleteFileResponse: LiveData<PublicResponseModel> = _deleteFileResponse
    private val _saveFavResponse = MutableLiveData<PublicResponseModel>()
    val saveFavResponse: LiveData<PublicResponseModel> = _saveFavResponse
    private val _deleteFavResponse = MutableLiveData<PublicResponseModel>()
    val deleteFavResponse: LiveData<PublicResponseModel> = _deleteFavResponse
    private val _cooperationResponse = MutableLiveData<PublicResponseModel>()
    val cooperationResponse: LiveData<PublicResponseModel> = _cooperationResponse
    private val _favList = MutableLiveData<FavFileResponse>()
    val favList: LiveData<FavFileResponse> = _favList
    private val _inboxResponse = MutableLiveData<InboxOutboxModel>()
    val inboxResponse: LiveData<InboxOutboxModel> = _inboxResponse
    private val _outboxResponse = MutableLiveData<InboxOutboxModel>()
    val outboxResponse: LiveData<InboxOutboxModel> = _outboxResponse
    private val _cooperationResponseListReceived = MutableLiveData<InboxOutboxModel>()
    val cooperationResponseListReceived: LiveData<InboxOutboxModel> =
        _cooperationResponseListReceived
    private val _cooperationResponseListSend = MutableLiveData<InboxOutboxModel>()
    val cooperationResponseListSend: LiveData<InboxOutboxModel> = _cooperationResponseListSend
    private val _myFiles = MutableLiveData<MyFilesResponse>()
    val myFiles: LiveData<MyFilesResponse> = _myFiles
    private val _setStatusResponse = MutableLiveData<PublicResponseModel>()
    val setStatusResponse: LiveData<PublicResponseModel> = _setStatusResponse

    enum class ItemType { SHOW_ALL, SHOW_SEEKER, SHOW_OWNER }
    enum class ReqSource { CATEGORY, SUB_CATEGORY }

    private var reqSource: AddItemViewModel.ReqSource = AddItemViewModel.ReqSource.CATEGORY
    fun getReqSource() = reqSource
    fun setReqSource(reqSource: AddItemViewModel.ReqSource) {
        this.reqSource = reqSource
    }

    private val _locationResponse = MutableLiveData<LocationResponse>()
    val locationResponse: LiveData<LocationResponse> = _locationResponse

    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList
    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList
    private val _regionList = MutableLiveData<List<RegionResponseData>>()
    val regionList: LiveData<List<RegionResponseData>> = _regionList

    private val _filesResponse = MutableLiveData<FileResponse>()
    val filesResponse: LiveData<FileResponse> = _filesResponse

    var filterFileData: FilterFileData? = null
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

    fun getFiles(activity: Activity, token: String, cityId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFiles(activity, token, cityId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _locationResponse.value =
                        Api.retrofitService.getAllFilesByCity(token = token, cityId = cityId)
                    Log.e("TAG", "getFileInfoById: ${_locationResponse.value}")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getFiles, ", e)
                }
            }
    }

    fun getFilterFiles(activity: Activity, token: String, filterFileData: FilterFileData) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFilterFiles(activity, token, filterFileData)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _locationResponse.value =
                        Api.retrofitService.filterFiles(token = token, filterFileData)
                    Log.e("TAG", "getFilterFiles: ${_locationResponse.value}")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getFilterFiles, ", e)
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

    fun getFileInfoById(activity: Activity, token: String, fileId: Int, userId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFileInfoById(activity, token, fileId, userId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                //resetFileAllData()
                try {
                    _fileAllData.value =
                        Api.retrofitService.getFileInfoById(token, fileId, userId)
                    Log.e("TAG", "getFileInfoById: ${fileAllData.value!!.data}")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    resetFileAllData()
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getFileInfoById, ", e)
                }
            }
    }

    fun deleteFile(activity: Activity, token: String, fileId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                deleteFile(activity, token, fileId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _deleteFileResponse.value =
                        Api.retrofitService.deleteFile(
                            token,
                            fileId
                        )
                    Log.e("TAG", "deleteFile: ${_deleteFileResponse.value} ")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, deleteFile, ", e)
                }
            }
    }

    fun saveFavFile(activity: Activity, token: String, userId: Int, fileId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                saveFavFile(activity, token, userId, fileId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _saveFavResponse.value =
                        Api.retrofitService.saveFavoriteFile(
                            token,
                            userId,
                            fileId
                        )
                    Log.e("TAG", "saveFavFile: ${_saveFavResponse.value.toString()} ")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, saveFavFile, ", e)
                }
            }
    }

    fun deleteFavFile(activity: Activity, token: String, userId: Int, fileId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                deleteFavFile(activity, token, userId, fileId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _deleteFavResponse.value =
                        Api.retrofitService.deleteFavoriteFiles(
                            token,
                            userId,
                            fileId
                        )
                    Log.e("TAG", "deleteFavFile: ${_deleteFavResponse.value.toString()} ")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, deleteFavFile, ", e)
                }
            }
    }

    fun sendCooperationRequest(
        activity: Activity,
        token: String,
        userId: Int,
        fileId: Int
    ) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                sendCooperationRequest(activity, token, userId, fileId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _cooperationResponse.value =
                        Api.retrofitService.sendCooperationRequest(
                            token,
                            userId,
                            fileId
                        )
                    Log.e(
                        "TAG",
                        "sendCooperationRequest: ${_cooperationResponse.value.toString()} "
                    )
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, sendCooperationRequest, ", e)
                }
            }
    }

    fun getFavoritesFile(activity: Activity, token: String, userId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFavoritesFile(activity, token, userId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _favList.value = Api.retrofitService.getFavoriteFiles(token, userId)
                    Log.e("TAG", "getFavoritesFile: ${_favList.value.toString()} ")
                    _favList.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getFavoritesFile, ", e)
                }
            }
    }

    fun getInbox(activity: Activity, userId: Int, token: String) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getInbox(activity, userId, token)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _inboxResponse.value =
                        Api.retrofitService.inboxByStatus(
                            token = token,
                            userId = userId,
                            status = null
                        )
                    Log.e("TAG", "getInbox: ${_inboxResponse.value.toString()} ")
                    _inboxResponse.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: java.lang.Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getInbox, ", e)
                }
            }
    }

    fun getOutbox(activity: Activity, userId: Int, token: String) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getOutbox(activity, userId, token)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _outboxResponse.value =
                        Api.retrofitService.outboxByStatus(token = token, userId = userId, null)
                    Log.e("TAG", "getOutbox: ${_outboxResponse.value.toString()} ")
                    _outboxResponse.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getOutbox, ", e)
                }
            }
    }

    fun getReceivedCooperation(activity: Activity, userId: Int, token: String) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getReceivedCooperation(activity, userId, token)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _cooperationResponseListReceived.value =
                        Api.retrofitService.inboxByStatus(
                            token = token,
                            userId = userId,
                            status = 1
                        )
                    Log.e(
                        "TAG",
                        "_cooperationResponseListReceived: ${_cooperationResponseListReceived.value.toString()} ",
                    )
                    cooperationResponseListReceived.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: java.lang.Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getReceivedCooperation, ", e)
                }
            }
    }

    fun getSendCooperation(activity: Activity, userId: Int, token: String) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getSendCooperation(activity, userId, token)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _cooperationResponseListSend.value =
                        Api.retrofitService.outboxByStatus(token = token, userId = userId, 1)
                    Log.e(
                        "TAG",
                        "_cooperationResponseListSend: ${_cooperationResponseListSend.value.toString()} ",
                    )
                    cooperationResponseListSend.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: java.lang.Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getSendCooperation, ", e)
                }
            }
    }

    fun getMyFiles(activity: Activity, userId: Int, token: String) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getMyFiles(activity, userId, token)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _myFiles.value =
                        Api.retrofitService.getFileInfoByUserId(token = token, userId = userId)
                    Log.e("TAG", "getMyFiles: ${_myFiles.value.toString()} ")
                    _myFiles.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: java.lang.Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, getMyFiles, ", e)
                }
            }
    }

    fun setAlertStatus(
        activity: Activity,
        token: String,
        fileRequestId: Int,
        userId: Int,
        status: Int
    ) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                setAlertStatus(activity, token, fileRequestId, userId, status)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _setStatusResponse.value =
                        Api.retrofitService.setAlertStatus(
                            token = token,
                            fileRequestId = fileRequestId,
                            userId = userId,
                            status = status
                        )
                    Log.e("TAG", "_setStatusResponse: ${_setStatusResponse.value.toString()} ")
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "MainViewModel, setAlertStatus, ", e)
                }
            }
    }

    fun resetSaveResponse() {
        _saveFavResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun resetFileAllData() {
        _fileAllData.value = FileDataResponse(null, null, null)
    }

    fun resetDeleteFavResponse() {
        _deleteFavResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun setFileAllDataForMyFiles(user: User, file: FileData) {
        file.user = user
        _fileAllData.value = FileDataResponse(result = true, data = file, null)
    }

    fun resetDeleteFileResponse() {
        _deleteFileResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun resetStatusResponse() {
        _setStatusResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun resetInboxResponse() {
        _inboxResponse.value = InboxOutboxModel(null, null, null)
    }

    fun resetOutboxResponse() {
        _outboxResponse.value = InboxOutboxModel(null, null, null)
    }

    fun resetCooperationResponse() {
        _cooperationResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun reseetLocationResponse() {
        _locationResponse.value = LocationResponse(null, listOf(), null)
    }
}
package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.MainActivity
import com.example.melkist.models.ChartData
import com.example.melkist.models.Charts
import com.example.melkist.models.FavFileResponse
import com.example.melkist.models.FileActions
import com.example.melkist.models.FileActionsData
import com.example.melkist.models.FileData
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.FileTypes
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.InboxOutboxModel
import com.example.melkist.models.LocationResponse
import com.example.melkist.models.MemberImproData
import com.example.melkist.models.MyFilesResponse
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.SuggestionItemListModel
import com.example.melkist.models.SuggestionModel
import com.example.melkist.models.User
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.getCorrectDate
import com.example.melkist.utils.getMonthFromNowToOneYearBefore
import com.example.melkist.utils.getMonthNameWithMonthNo
import com.example.melkist.utils.getPersianMonthNumber
import com.example.melkist.utils.getPersianYear
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    fun setNoDataStatus() {
        _status.value = ApiStatus.NO_DATA
    }

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
    private val _aiSuggestedResponse = MutableLiveData<SuggestionModel>()
    val aiSuggestedResponse: LiveData<SuggestionModel> = _aiSuggestedResponse
    private val _aiSuggestionList = MutableLiveData<List<SuggestionItemListModel>>()
    val aiSuggestionList: LiveData<List<SuggestionItemListModel>> = _aiSuggestionList
    private val _saveActionResponse = MutableLiveData<PublicResponseModel>()
    val saveActionResponse: LiveData<PublicResponseModel> = _saveActionResponse
    private val _fileActionsResponse = MutableLiveData<FileActions>()
    val fileActionsResponse: LiveData<FileActions> = _fileActionsResponse
    private val _subsetUsersActionsResponse = MutableLiveData<FileActions>()
    val subsetUsersActionsResponse: LiveData<FileActions> = _subsetUsersActionsResponse
    private val _memberImproChartResponse = MutableLiveData<Charts>()
    val memberImproChartResponse: LiveData<Charts> = _memberImproChartResponse
    private val _workPeakResponse = MutableLiveData<Charts>()
    val workPeakResponse: LiveData<Charts> = _workPeakResponse

    enum class ItemType { SHOW_ALL, SHOW_SEEKER, SHOW_OWNER }

    private val _locationResponse = MutableLiveData<LocationResponse>()
    val locationResponse: LiveData<LocationResponse> = _locationResponse

    var filterFileData: FilterFileData? = null
    var filterFavData: FilterFileData? = null
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

    val filterItemsForSuggestions: MutableList<Int> = mutableListOf()

    ////////////////      CALCULATIONS        /////////////////
    fun getTypeId(requestCode: ItemType): Int {
        val fileTypes = FileTypes()
        return when (requestCode) {
            ItemType.SHOW_SEEKER -> fileTypes.seeker.id
            ItemType.SHOW_OWNER -> fileTypes.owner.id
            else -> -1
        }
    }

    fun hasChooseTypeInfos(): Boolean {
        return itemType != ItemType.SHOW_ALL  && catId != null && subCatId != null && catTitle != null && subCatTitle != null
    }

    fun getStatStaffCount(fileActionsData: List<FileActionsData>?): List<Array<String>> {
        val resultList = mutableListOf<Array<String>>()
        val listOfUsersId = getListOfUserIdsForCalculationStatStaff(fileActionsData)
        listOfUsersId.forEach { subUserId ->
            val userStatArray = getUserStat(subUserId, fileActionsData)
            resultList.add(userStatArray)
        }
        resultList.sortByDescending { it[7] }
        return resultList
    }

    fun getStaffImproCount(fileActionsData: List<FileActionsData>?): List<MemberImproData> {
        val resultList = mutableListOf<MemberImproData>()
        val listOfUsersId = getListOfUserIdsForCalculationStatStaff(fileActionsData)
        listOfUsersId.forEach { subUserId ->
            val item = MemberImproData(
                userId = subUserId,
                userName = getUserNameWithId(subUserId, fileActionsData ?: listOf()),
                chartData = getUserChartData(subUserId, fileActionsData ?: listOf())
            )
            resultList.add(item)
        }
        return resultList
    }

    private fun getListOfUserIdsForCalculationStatStaff(
        fileActionsData: List<FileActionsData>?
    ): List<Int> {
        val list = mutableListOf<Int>()
        fileActionsData?.forEach { actionObject ->
            actionObject.performerUser?.apply {
                if (!list.contains(id)) list.add(id!!)
            }
        }
        Log.e("TAG", "getListOfUserIdsForCalculationStatStaff: $list")
        return list
    }

    private fun getUserChartData(id: Int, actionList: List<FileActionsData>): List<ChartData> {
        val resultList = mutableListOf<ChartData>()
        val monthList = getMonthFromNowToOneYearBefore(getPersianMonthNumber())
        monthList.forEach { pair ->
            var sum = 0
            val year = pair.first
            val month = pair.second
            actionList.forEach { actionData ->
                val realDate = getCorrectDate(actionData.action?.actionDate ?: 0)
                if (actionData.performerUser?.id == id &&
                    getPersianYear(realDate) == year &&
                    getPersianMonthNumber(realDate) == month
                )
                    sum++
            }
            resultList.add(ChartData(getMonthNameWithMonthNo(month), sum.toDouble()))
        }
        return resultList
    }

    private fun getUserNameWithId(id: Int, actionList: List<FileActionsData>): String {
        var stringResult = ""
        if (actionList.isNotEmpty())
            actionList.forEach { actionData ->
                if (actionData.performerUser!!.id == id) {
                    stringResult =
                        actionData.performerUser.firstName + " " + actionData.performerUser.lastName
                    return@forEach
                }
            }
        return stringResult
    }

    private fun getUserStat(userId: Int?, fileActionsData: List<FileActionsData>?): Array<String> {
        val arrayResult: Array<String> = Array(8) { "" }
        var fileCreationCount = 0
        var visitCount = 0
        var meetingCount = 0
        var contractCount = 0
        var consultCount = 0

        val subUserList = fileActionsData?.filter { it.performerUser!!.id == userId }

        Log.e("TAG", "getUserStat: $subUserList")

        subUserList?.forEach { userActions ->

            when (userActions.action!!.id) {
                1 -> fileCreationCount++
                2 -> visitCount++
                3 -> meetingCount++
                4 -> contractCount++
                5 -> consultCount++
                else -> Log.e(
                    "TAG",
                    "getFileCreationCount: there is another ID number, caution",
                )
            }
        }

        arrayResult[1] =
            subUserList?.get(0)?.performerUser?.firstName + " " + subUserList?.get(0)?.performerUser?.lastName
        arrayResult[2] = fileCreationCount.toString()
        arrayResult[3] = visitCount.toString()
        arrayResult[4] = meetingCount.toString()
        arrayResult[5] = contractCount.toString()
        arrayResult[6] = consultCount.toString()
        arrayResult[7] =
            (fileCreationCount + visitCount + meetingCount + contractCount + consultCount).toString()
        return arrayResult
    }

    ///////////////         INTERNET          ////////////////
    fun getFiles(activity: Activity, token: String?, cityId: Int?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getFiles(activity, token, cityId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _locationResponse.value =
                    Api.retrofitService.getAllFilesByCity(token = token, cityId = cityId)
                Log.e("TAG", "getFileInfoById: ${_locationResponse.value}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope,
                    "${(activity as MainActivity).user?.id}, MainViewModel, getFiles, ",
                    e
                )
            }
        }
    }

    fun getFilterFiles(activity: Activity, token: String?, filterFileData: FilterFileData) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getFilterFiles(activity, token, filterFileData)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _locationResponse.value =
                    Api.retrofitService.filterFiles(token = token, filterFileData)
                Log.e("TAG", "getFilterFiles: ${_locationResponse.value}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope,
                    "${(activity as MainActivity).user?.id}, MainViewModel, getFilterFiles, ",
                )
            }
        }
    }

    fun getFileInfoById(activity: Activity, token: String?, fileId: Int, userId: Int?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getFileInfoById(activity, token, fileId, userId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            //resetFileAllData()
            try {
                _fileAllData.value = Api.retrofitService.getFileInfoById(token, fileId, userId)
                Log.e("TAG", "getFileInfoById: ${fileAllData.value!!.data}")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                resetFileAllData()
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, getFileInfoById, ", e
                )
            }
        }
    }

    fun deleteFile(activity: Activity, token: String?, fileId: Int) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            deleteFile(activity, token, fileId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _deleteFileResponse.value = Api.retrofitService.deleteFile(
                    token, fileId
                )
                Log.e("TAG", "deleteFile: ${_deleteFileResponse.value} ")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope,
                    "${(activity as MainActivity).user?.id}, MainViewModel, deleteFile, ",
                    e
                )
            }
        }
    }

    fun saveFavFile(activity: Activity, token: String?, userId: Int?, fileId: Int) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            saveFavFile(activity, token, userId, fileId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _saveFavResponse.value = Api.retrofitService.saveFavoriteFile(
                    token, userId, fileId
                )
                Log.e("TAG", "saveFavFile: ${_saveFavResponse.value.toString()} ")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, saveFavFile, ", e
                )
            }
        }
    }

    fun deleteFavFile(activity: Activity, token: String?, userId: Int?, fileId: Int) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            deleteFavFile(activity, token, userId, fileId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _deleteFavResponse.value = Api.retrofitService.deleteFavoriteFiles(
                    token, userId, fileId
                )
                Log.e("TAG", "deleteFavFile: ${_deleteFavResponse.value.toString()} ")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, deleteFavFile, ", e
                )
            }
        }
    }

    fun sendCooperationRequest(
        activity: Activity, token: String?, userId: Int?, fileId: Int
    ) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            sendCooperationRequest(activity, token, userId, fileId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _cooperationResponse.value = Api.retrofitService.sendCooperationRequest(
                    token, userId, fileId
                )
                Log.e(
                    "TAG", "sendCooperationRequest: ${_cooperationResponse.value.toString()} "
                )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, sendCooperationRequest, ", e
                )
            }
        }
    }

    fun getFavoritesFile(activity: Activity, token: String?, userId: Int?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getFavoritesFile(activity, token, userId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _favList.value = Api.retrofitService.getFavoriteFiles(token, userId)
                Log.e("TAG", "getFavoritesFile: ${_favList.value.toString()} ")
                _favList.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, getFavoritesFile, ", e
                )
            }
        }
    }

    fun getInbox(activity: Activity, userId: Int?, token: String?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getInbox(activity, userId, token)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _inboxResponse.value = Api.retrofitService.inboxByStatus(
                    token = token, userId = userId, status = null
                )
                Log.e("TAG", "getInbox: ${_inboxResponse.value.toString()} ")
                _inboxResponse.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: java.lang.Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(viewModelScope, "$userId, MainViewModel, getInbox, ", e)
            }
        }
    }

    fun getOutbox(activity: Activity, userId: Int?, token: String?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getOutbox(activity, userId, token)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _outboxResponse.value =
                    Api.retrofitService.outboxByStatus(token = token, userId = userId, null)
                Log.e("TAG", "getOutbox: ${_outboxResponse.value.toString()} ")
                _outboxResponse.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(viewModelScope, "$userId, MainViewModel, getOutbox, ", e)
            }
        }
    }

    fun getReceivedCooperation(activity: Activity, userId: Int?, token: String?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getReceivedCooperation(activity, userId, token)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _cooperationResponseListReceived.value = Api.retrofitService.inboxByStatus(
                    token = token, userId = userId, status = 1
                )
                Log.e(
                    "TAG",
                    "_cooperationResponseListReceived: ${_cooperationResponseListReceived.value.toString()} ",
                )
                cooperationResponseListReceived.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: java.lang.Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, getReceivedCooperation, ", e
                )
            }
        }
    }

    fun getSendCooperation(activity: Activity, userId: Int?, token: String?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getSendCooperation(activity, userId, token)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _cooperationResponseListSend.value =
                    Api.retrofitService.outboxByStatus(token = token, userId = userId, 1)
                Log.e(
                    "TAG",
                    "_cooperationResponseListSend: ${_cooperationResponseListSend.value.toString()} ",
                )
                cooperationResponseListSend.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: java.lang.Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, getSendCooperation, ", e
                )
            }
        }
    }

    fun getMyFiles(activity: Activity, userId: Int?, token: String?) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getMyFiles(activity, userId, token)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _myFiles.value =
                    Api.retrofitService.getFileInfoByUserId(token = token, userId = userId)
                Log.e("TAG", "getMyFiles: ${_myFiles.value.toString()} ")
                _myFiles.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }
            } catch (e: java.lang.Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(viewModelScope, "$userId, MainViewModel, getMyFiles, ", e)
            }
        }
    }

    fun setAlertStatus(
        activity: Activity, token: String?, fileRequestId: Int, userId: Int?, status: Int
    ) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            setAlertStatus(activity, token, fileRequestId, userId, status)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _setStatusResponse.value = Api.retrofitService.setAlertStatus(
                    token = token, fileRequestId = fileRequestId, userId = userId, status = status
                )
                Log.e("TAG", "_setStatusResponse: ${_setStatusResponse.value.toString()} ")
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, setAlertStatus, ", e
                )
            }
        }
    }

    fun getAiSuggestedFiles(
        activity: Activity, token: String?, userId: Int?
    ) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getAiSuggestedFiles(activity, token, userId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _aiSuggestedResponse.value = Api.retrofitService.getSuggestedFile(
                    token = token, userId = userId
                )
                Log.e("TAG", "_aiSuggestedResponse: ${_aiSuggestedResponse.value.toString()} ")
                _aiSuggestedResponse.value?.apply {
                    if (data.isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                    else _status.value = ApiStatus.DONE
                }

                val list: MutableList<SuggestionItemListModel> = mutableListOf()
                _aiSuggestedResponse.value?.data?.forEach { item ->
                    item.similarFiles?.forEach { fileData ->
                        list.add(SuggestionItemListModel(item.myFile, fileData))
                    }
                    _aiSuggestionList.value = list
                }
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, getAiSuggestedFiles, ", e
                )
            }
        }
    }


    fun saveAction(
        activity: Activity,
        token: String?,
        fileId: Int?,
        userId: Int?,
        fileActionId: Int?,
        actionDate: Long?,
        actionOwnerName: String?,
        actionOwnerMobile: String?
    ) {
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            saveAction(
                activity,
                token,
                fileId,
                userId,
                fileActionId,
                actionDate,
                actionOwnerName,
                actionOwnerMobile
            )
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e(
                    "TAG",
                    "saveAction: token = $token\n" + "fileId = $fileId\n" + "userId = $userId\n" + "fileActionId = $fileActionId\n" + "actionDate = $actionDate\n" + "ownerName = $actionOwnerName\n" + "ownerMobile = $actionOwnerMobile",
                )


                _saveActionResponse.value = Api.retrofitService.saveAction(
                    token = token,
                    fileId = fileId,
                    userId = userId,
                    fileActionId = fileActionId,
                    actionDate = actionDate,
                    ownerName = actionOwnerName,
                    ownerMobile = actionOwnerMobile
                )
                Log.e("TAG", "_saveActionResponse: ${_saveActionResponse.value.toString()} ")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "$userId, MainViewModel, saveAction, ", e
                )
            }
        }
    }

    fun getActionsOfFile(activity: Activity, fileId: Int) {
        val user = (activity as MainActivity).user
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getActionsOfFile(activity, fileId)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _fileActionsResponse.value = Api.retrofitService.getActionsOfFile(
                    token = user?.token, fileId = fileId
                )
                Log.e("TAG", "_fileActionsResponse: ${fileActionsResponse.value.toString()} ")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "${user?.id}, MainViewModel, getActionsOfFile, ", e
                )
            }
        }
    }

    fun getUserSubsetActionStat(activity: Activity) {
        val user = (activity as MainActivity).user
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getUserSubsetActionStat(activity)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _subsetUsersActionsResponse.value = Api.retrofitService.getSubsetUsersActions(
                    token = user?.token, userId = user?.id
                )
                Log.e(
                    "TAG",
                    "_subsetUsersActionsResponse: ${subsetUsersActionsResponse.value.toString()} "
                )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "${user?.id}, MainViewModel, getUserSubsetStat, ", e
                )
            }
        }
    }

    fun getMemberImproChart(activity: Activity, period: Int) {
        val user = (activity as MainActivity).user
        if (!isOnline(activity)) internetProblemDialog(activity) { _, _ ->
            getMemberImproChart(activity, period)
        }
        else viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _memberImproChartResponse.value =
                    Api.retrofitService.getSumOfSubsetUsersActionsPeriodically(
                        token = user?.token, userId = user?.id, period = period
                    )
                Log.e(
                    "TAG",
                    "_memberImproChart: ${memberImproChartResponse.value.toString()} "
                )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                handleSystemException(
                    viewModelScope, "${user?.id}, MainViewModel, getMemberImproChart, ", e
                )
            }
        }
    }


///////////////      RESETTING         ////////////////

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

    fun resetSaveResponse() {
        _saveFavResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun resetFileAllData() {
        _fileAllData.value = FileDataResponse(null, null, null)
    }

    fun resetDeleteFavResponse() {
        _deleteFavResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun setFileAllDataForMyFiles(user: User?, file: FileData) {
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

    fun resetLocationResponse() {
        _locationResponse.value = LocationResponse(null, listOf(), null)
    }

    fun resetSaveActionResponse() {
        _saveActionResponse.value = PublicResponseModel(null, null, listOf())
    }

    fun resetFileActionsResponse() {
        _fileActionsResponse.value = FileActions(null, null, null)
    }

    fun resetSubsetUsersActionsResponse() {
        _subsetUsersActionsResponse.value = FileActions(null, null, null)

    }

    fun resetWorkPeakResponse() {
        _workPeakResponse.value = Charts(null, null, null)
    }
}
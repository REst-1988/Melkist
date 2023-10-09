package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.PcrsData
import com.example.melkist.models.RegionResponseData
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class ChooseCrViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status

    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList
    private val _regionList = MutableLiveData<List<RegionResponseData>>()
    val regionList: LiveData<List<RegionResponseData>> = _regionList

    fun emptyRegionList() {
        _regionList.value = listOf()
    }

    fun getRegion(activity: Activity, cityId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getRegion(activity, cityId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _regionList.value =
                        Api.retrofitService.getRegionsByCity(cityId).data
                    Log.e("TAG", "_regionList: ${_regionList.value.toString()} ")
                    _regionList.value?.apply {
                        if (isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "ChooseCrViewModel, getRegion, ", e)
                }
            }
    }

    fun getProvinces(activity: Activity) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getProvinces(activity)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _pcrsList.value =
                        Api.retrofitService.getGetProvinces().data?: listOf()
                    Log.e("TAG", "_pcrsList: ${_pcrsList.value.toString()} ")
                    _pcrsList.value?.apply {
                        if (isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "ChooseCrViewModel, getProvinces, ", e)
                }
            }
    }

    fun getCity(activity: Activity, provinceId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getCity(activity, provinceId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _pcrsList.value =
                        Api.retrofitService.getCitiesByProvinceId(provinceId).data?: listOf()
                    Log.e("TAG", "_pcrsList: ${_pcrsList.value.toString()} ")
                    _pcrsList.value?.apply {
                        if (isNullOrEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "ChooseCrViewModel, getCity, ", e)
                }
            }
    }

    fun emptyPcList() {
        _pcrsList.value = listOf()
    }
}
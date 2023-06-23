package com.example.melkist.viewmodels.dialogviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.FileDataResponse
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch

class BottomSheetFileDialogViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _fileAllData = MutableLiveData<FileDataResponse>()
    val fileAllData: LiveData<FileDataResponse> = _fileAllData

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
}
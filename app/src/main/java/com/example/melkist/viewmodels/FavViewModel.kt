package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.FavFileResponse
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.network.Api
import com.example.melkist.network.`interface`.ApiService
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch

class FavViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _favList = MutableLiveData<FavFileResponse>()
    val favList: LiveData<FavFileResponse> = _favList

    fun getFavoritesFile (token: String, userId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _favList.value = Api.retrofitService.getFavoriteFiles(token, userId)
                _status.value = ApiStatus.DONE
            } catch (e: Exception){
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }
}
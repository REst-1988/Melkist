package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.LoginResponseModel
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _status = MutableLiveData(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private var _loginResponse = MutableLiveData<LoginResponseModel>()
    val loginResponse: LiveData<LoginResponseModel> = _loginResponse

    fun login(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _loginResponse.value =
                    Api.retrofitService.login(username, password)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }
}
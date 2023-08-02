package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.LoginResponseModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _status = MutableLiveData(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private var _loginResponse = MutableLiveData<LoginResponseModel>()
    val loginResponse: LiveData<LoginResponseModel> = _loginResponse

    fun login(
        activity: Activity,
        username: String,
        password: String,
        firebaseToken: String?
    ) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                login(
                    activity,
                    username,
                    password,
                    firebaseToken
                )
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _loginResponse.value =
                        Api.retrofitService.login(username, password, firebaseToken)
                    Log.e("TAG", "callServerAppVersion: ${_loginResponse.value.toString()}")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "LoginViewModel, login, ", e)
                }
            }
    }
}
package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.AppVersionResponse
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private var _appVersionResponse =
        MutableLiveData<AppVersionResponse>()
    val appVersionResponse: LiveData<AppVersionResponse> = _appVersionResponse

    fun callServerAppVersion(
        activity: Activity,
        userId: Int?,
        firebaseToken: String?,
        appVersion: String
    ) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                callServerAppVersion(
                    activity,
                    userId,
                    firebaseToken,
                    appVersion
                )
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _appVersionResponse.value =
                        Api.retrofitService.versionControl(
                            userId,
                            firebaseToken,
                            appVersion
                        )
                    Log.e("TAG", "callServerAppVersion: ${_appVersionResponse.value.toString()}")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    handleSystemException(viewModelScope, "SplashViewModel, callServerAppVersion, ", e)
                }
            }
    }
}
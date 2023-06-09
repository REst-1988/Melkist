package com.example.melkist.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgetPassViewModel : ViewModel() {

    private var mobileNo: String = ""
    private var nationalCode: Long = 0

    var password: String = ""

    fun getMobileNo() = mobileNo
    fun setMobileNo(mobile: String) { mobileNo = mobile }
    fun getNationalCode() = nationalCode
    fun setNationalCode(code: Long) { nationalCode = code }

    private val _isTimeUp = MutableLiveData<Boolean>()
    val isTimeUp: LiveData<Boolean> = _isTimeUp
    private var _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft
    private lateinit var timer: CountDownTimer

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _verificationCodeResponse = MutableLiveData<PublicResponseModel>()
    val verificationCodeResponse: LiveData<PublicResponseModel> = _verificationCodeResponse

    private val _verifyResponse = MutableLiveData<PublicResponseModel>()
    val verifyResponse: LiveData<PublicResponseModel> = _verifyResponse

    private val _changePassResponse = MutableLiveData<PublicResponseModel>()
    val changePassResponse: LiveData<PublicResponseModel> = _changePassResponse

    private fun setupTimer() {
        if (timeLeft.value == 0) {
            // 60 second time for receive SMS
            val totalTime: Long = 60000
            timer = object : CountDownTimer(totalTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _timeLeft.value = (millisUntilFinished / 1000).toInt()
                }
                override fun onFinish() {
                    _isTimeUp.value = true
                }
            }
            timer.start()
        }
    }

    fun resetTimer() {
        if (_isTimeUp.value != false)
            _isTimeUp.value = false
        setupTimer()
    }

    fun stopTimer() {
        _isTimeUp.value = true
        timer.cancel()
        _timeLeft.value = 0
    }

    fun getNcodeMobileVerificationCode(ncode: String, mobile: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _verificationCodeResponse.value =
                    Api.retrofitService.getForgetPasswordVerificationCode(mobile, ncode)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun sendMobileVerificationCode(mobile: String, code: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e("TAG", "sendMobileVerificationCode: 1", )
                _verifyResponse.value =
                    Api.retrofitService.verifyCode(mobile, code)

                Log.e("TAG", "sendMobileVerificationCode: 2", )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun requestChangePasswordByMobile() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _changePassResponse.value =
                    Api.retrofitService.changePasswordByMobile(getMobileNo(), newPassword = password)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun restVerificationResponse(vr: LiveData<PublicResponseModel>) {
        if (vr.value != null) {
            vr.value!!.result = null
            vr.value!!.message = ""
            vr.value!!.errors = listOf()

        }
    }

    fun isResponseOk(vr: LiveData<PublicResponseModel>): Boolean = vr.value != null
            && vr.value!!.result != null
            && vr.value!!.result == true

    fun isResponseNotOk(vr: LiveData<PublicResponseModel>): Boolean = vr.value != null
            && vr.value!!.result != null
            && vr.value!!.result == false
}
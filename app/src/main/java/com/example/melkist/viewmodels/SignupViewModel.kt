package com.example.melkist.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.Roles
import com.example.melkist.models.VerificationResponseModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SignupViewModel : ViewModel() {

    val STATE_REAL_ESTATE = 0
    val STATE_USER = 1
    val SUB_STATE_MANAGER = 2
    val SUB_STATE_SUPERVISER = 3
    val SUB_STATE_CONSOLTANT = 4
    val SUB_STATE_NORMAL_USER = 5
    val SUB_STATE_DEALER = 6

    private var condition: Int = STATE_REAL_ESTATE
    private var subCondition: Int = SUB_STATE_MANAGER

    /*private */var realEstateName: String? = null
    /*private*/ /*lateinit*/ var firstName: String = ""
    /*private*/ /*lateinit*/ var lastName: String = ""
    /*private*/ /*lateinit*/ var mobileNo: String = ""
    /*private*/ var nationalCode: Long = 0
    /*private*/ var email: String? = null
    /*private*/ /*lateinit*/ var password: String = ""
    /*private*/ var roleId: Int = 0
    /*private*/ var parentId: Int? = null
    /*private*/ var isVerify: Boolean = false
    /*private*/ /*lateinit*/ /*var user: User */

    private val _isTimeUp = MutableLiveData<Boolean>()
    val isTimeUp: LiveData<Boolean> = _isTimeUp
    private var _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft
    private lateinit var timer: CountDownTimer

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _verificationCodeResponse = MutableLiveData<VerificationResponseModel>()
    val verificationCodeResponse: LiveData<VerificationResponseModel> = _verificationCodeResponse

    private val _verifyResponse = MutableLiveData<VerificationResponseModel>()
    val verifyResponse: LiveData<VerificationResponseModel> = _verifyResponse


    fun getRoles(): Roles = Roles()
    fun getCondition(): Int = condition
    fun setCondition(state: Int) {
        condition = state
    }

    fun getSubCondition(): Int = subCondition
    fun setSubCondition(subCondition: Int) {
        this.subCondition = subCondition
    }

    fun createUser(
        realEstateName: String?,
        firstName: String,
        lastName: String,
        mobileNo: String,
        nationalCode: Long,
        email: String?,
        password: String
    ) {
        this.realEstateName = realEstateName
        this.firstName = firstName
        this.lastName = lastName
        this.mobileNo = mobileNo
        this.nationalCode = nationalCode
        this.email = email
        this.password = password
    }

    // For Page2ReceiveVerificationSmsFragment
    private fun setupTimer() {
        if (timeLeft.value == 0) {
            // 60 second time for receive SMS
            val totalTime: Long = 60000
            timer = object : CountDownTimer(totalTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _timeLeft.value = (millisUntilFinished / 1000).toInt()
                    Log.e("TAG", "onTick: ${timeLeft.value}")
                }
                override fun onFinish() {
                    _isTimeUp.value = true
                }
            }
            timer.start()
        }
    }
    // For Page2ReceiveVerificationSmsFragment
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

    fun getMobileVerificationCode(mobile: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            Log.e("TAG", "getMobileVerificationCode: test 1")
            try {
                Log.e("TAG", "getMobileVerificationCode: test 2")
                _verificationCodeResponse.value = Api.retrofitService.getVerificationCode(mobile)
                Log.e("TAG", "getMobileVerificationCode: test 3")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.e("TAG", "getMobileVerificationCode: test 4")
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
                //_verificationCodeResponse.value = VerificationResponse(false, "ERROR","")
            }
        }
    }

    fun sendMobileVerificationCode(mobile: String, code: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e("TAG", "sendMobileVerificationCode: test 2")
                _verifyResponse.value =
                    Api.retrofitService.getVerifyResponse(mobile, code)
                Log.e("TAG", "sendMobileVerificationCode: test 3")
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.e("TAG", "sendMobileVerificationCode: test 4")
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
                //_verificationResponse.value = VerificationResponse(false, "ERROR")
            }
        }
    }

    fun restVerificationResponse(vr: LiveData<VerificationResponseModel>) {
        if (vr.value != null) {
            vr.value!!.result = null
            vr.value!!.message = ""
            vr.value!!.errors = listOf()

        }
    }
    // For Page1EnterNcodePhoneFragment & Page2ReceiveVerificationSmsFragment
    fun isResponseOk(vr: LiveData<VerificationResponseModel>): Boolean = vr.value != null
            && vr.value!!.result != null
            && vr.value!!.result == true
    // For Page1EnterNcodePhoneFragment & Page2ReceiveVerificationSmsFragment
    fun isResponseNotOk(vr: LiveData<VerificationResponseModel>): Boolean = vr.value != null
            && vr.value!!.result != null
            && vr.value!!.result == false

}
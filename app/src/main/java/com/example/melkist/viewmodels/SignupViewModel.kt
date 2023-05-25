package com.example.melkist.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.PcrsData
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.Roles
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SignupViewModel() : ViewModel() {
    enum class Condition { CHOOSE, STATE_REAL_ESTATE, STATE_USER }
    enum class Pcrs { PROVINCE, CITY, REAL_ESTATE, SUPERVISOR }

    fun getRoles(): Roles = Roles()
    val SUB_STATE_CHOOSE = 0
    val SUB_STATE_MANAGER = getRoles().manager.id
    val SUB_STATE_SUPERVISER = getRoles().supervisor.id
    val SUB_STATE_CONSOLTANT = getRoles().consultant.id
    val SUB_STATE_NORMAL_USER = getRoles().normalUser.id
    val SUB_STATE_DEALER = getRoles().dealer.id

    private var condition: Condition = Condition.CHOOSE
    var subConditionRoleId: Int = SUB_STATE_CHOOSE
    var provinceId: Int = 0 // 17

    var provinceTitle: String = ""
    var cityId: Int = 0 // 733
    var cityTitle: String = ""
    var supervisorId = 0
    var supervisorTitle: String = ""
    var parentId: Int? = null
    var realEstateNameForManager: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var mobileNo: String = ""
    var nationalCode: Long = 0
    var email: String? = null
    var password: String = ""


    var realEstateId: Int? = null
    var realEstateTitle: String? = null

    var pcrsCondition: Pcrs = Pcrs.PROVINCE

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

    private val _registerResponse = MutableLiveData<PublicResponseModel>()
    val registerResponse: LiveData<PublicResponseModel> = _registerResponse

    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList

    fun getCondition(): Condition = condition
    fun setCondition(state: Condition) {
        condition = state
    }

    fun getSubCondition(): Int = subConditionRoleId
    fun setSubCondition(subCondition: Int) {
        this.subConditionRoleId = subCondition
    }

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

    fun sendMobileVerificationCode(mobile: String, code: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _verifyResponse.value =
                    Api.retrofitService.verifyCode(mobile, code)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getProvinces() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _pcrsList.value =
                    Api.retrofitService.getGetProvinces().data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getCities() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _pcrsList.value =
                    Api.retrofitService.getCitiesByProvinceId(provinceId).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getRealEstate() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _pcrsList.value =
                    Api.retrofitService.getRealEstateByCityId(cityId).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getSuperVisor() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _pcrsList.value =
                    Api.retrofitService.getSuperVisorByManagerId(parentId!!).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun checkSignupData(
        name: String?, lastName: String?, title: String?,
        cityId: Int?, mobile: String, nationalCode: String, email: String?,
        roleId: Int
    ) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _verificationCodeResponse.value =
                    Api.retrofitService.checkSignupData(
                        name, lastName, title, cityId,
                        mobile, nationalCode, email, roleId
                    )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }


    fun registerUserRealstate() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e(
                    "TAG", "registerUserRealstate: test " +
                            String.format(
                                "%s  %s\n %s  %s\n %s  %s\n %s  %s\n %s  %s",
                                realEstateNameForManager,
                                cityId,
                                firstName!!,
                                lastName!!,
                                mobileNo,
                                nationalCode.toString(),
                                email,
                                password,
                                parentId ?: 1,
                                subConditionRoleId
                            )
                )
                _registerResponse.value =
                    Api.retrofitService.registerUserRealEstate(
                        realEstateNameForManager,
                        cityId,
                        firstName!!,
                        lastName!!,
                        mobileNo,
                        nationalCode.toString(),
                        email,
                        password,
                        parentId ?: 1 /*every user is a child of head manager*/,
                        subConditionRoleId
                    )
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun choosingItemAction(pcrs: PcrsData) {
        when (pcrsCondition) {
            SignupViewModel.Pcrs.PROVINCE -> {
                provinceId = pcrs.id!!
                provinceTitle = pcrs.title!!
            }
            SignupViewModel.Pcrs.CITY -> {
                cityId = pcrs.id!!
                cityTitle = pcrs.title!!
            }
            SignupViewModel.Pcrs.REAL_ESTATE -> {
                realEstateId = pcrs.id!!
                realEstateTitle = pcrs.title!!
                Log.e("TAG", "choosingItemAction: ${pcrs.user?.id}")
                parentId = pcrs.user?.id
            }
            else -> {
                supervisorId = pcrs.id!!
                supervisorTitle = pcrs.title!!
                parentId = pcrs.id
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

    fun resetSignupFieldsByChoosingMainField() {
        subConditionRoleId = SUB_STATE_CHOOSE
        parentId = null
        provinceId = 0
        provinceTitle = ""
        cityId = 0
        cityTitle = ""
        realEstateId = null
        realEstateTitle = null
        supervisorId = 0
        supervisorTitle = ""
        realEstateNameForManager = null
    }

    fun resetSignupFieldsByChoosingCategory() {
        parentId = if (getSubCondition() == SUB_STATE_MANAGER) null else parentId
        provinceId = 0
        provinceTitle = ""
        cityId = 0
        cityTitle = ""
        realEstateId = null
        realEstateTitle = null
        supervisorId = 0
        supervisorTitle = ""
        realEstateNameForManager = null
    }

    fun resetSignupFieldsByProvince() {
        cityId = 0
        cityTitle = ""
        realEstateId = null
        realEstateTitle = null
        supervisorId = 0
        supervisorTitle = ""
        realEstateNameForManager = null
    }

    fun resetSignupFieldsByCity() {
        realEstateId = null
        realEstateTitle = null
        supervisorId = 0
        supervisorTitle = ""
        realEstateNameForManager = null
    }

    fun resetSignupFieldsByRealEstate() {
        supervisorId = 0
        supervisorTitle = ""
        realEstateNameForManager = null
    }

    fun resetSignupFieldsBySupervisor() {
        realEstateNameForManager = null
    }

    fun emptyList() {
        _pcrsList.value = listOf()
    }
}
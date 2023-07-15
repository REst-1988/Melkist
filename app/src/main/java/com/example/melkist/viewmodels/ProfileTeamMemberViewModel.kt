package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.User
import com.example.melkist.models.Users
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileTeamMemberViewModel : ViewModel() {
    private val _teamMembers = MutableLiveData<Users>()
    val teamMembers: LiveData<Users> = _teamMembers
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status

    var user: User? = null

    fun getTeamMembers(token: String, userId: Int, roleId: Int) {
        viewModelScope.launch {
            Log.e("TAG", "getTeamMembers: launch proceed", )
            _status.value = ApiStatus.LOADING
            try {
                _teamMembers.value = Api.retrofitService.getAllUserSubset(
                    token, userId, roleId
                )
                _status.value = ApiStatus.DONE
                Log.e("TAG", "getTeamMembers: ${teamMembers.value?.data}", )
            }catch (e: Exception){
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }
}
package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.User
import com.example.melkist.models.Users
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class ProfileTeamMemberViewModel : ViewModel() {

    private val _teamMembers = MutableLiveData<Users>()
    val teamMembers: LiveData<Users> = _teamMembers
    private val _deleteTeamMemberResponse = MutableLiveData<PublicResponseModel>()
    val deleteTeamMemberResponse: LiveData<PublicResponseModel> = _deleteTeamMemberResponse
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status

    var teamMember: User? = null

    fun getTeamMembers(activity: Activity, token: String, userId: Int, roleId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getTeamMembers(
                    activity,
                    token,
                    userId,
                    roleId
                )
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _teamMembers.value = Api.retrofitService.getAllUserSubset(
                        token, userId, roleId
                    )
                    Log.e("TAG", "_teamMembers: ${_teamMembers.value.toString()} ")
                    _teamMembers.value?.apply {
                        if (data!!.isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    handleSystemException(viewModelScope, "ProfileTeamMemberViewModel, getTeamMembers, ", e)
                    _status.value = ApiStatus.ERROR
                }
            }
    }

    fun deleteTeamMembers(activity: Activity, token: String, userId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                deleteTeamMembers(
                    activity,
                    token,
                    userId
                )
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    _deleteTeamMemberResponse.value = Api.retrofitService.deleteUser(
                        token, userId
                    )
                    Log.e("TAG", "_deleteTeamMemberResponse: ${_deleteTeamMemberResponse.value.toString()} ")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    handleSystemException(viewModelScope, "ProfileTeamMemberViewModel, deleteTeamMembers, ", e)
                    _status.value = ApiStatus.ERROR
                }
            }
    }
}
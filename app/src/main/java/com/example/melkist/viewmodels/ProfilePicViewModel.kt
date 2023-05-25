package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.melkist.utils.ApiStatus

class ProfilePicViewModel: ViewModel() {

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
}
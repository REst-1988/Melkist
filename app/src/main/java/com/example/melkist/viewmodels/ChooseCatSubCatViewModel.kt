package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.FileTypes
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.OWNER_ITEM_TYPE
import com.example.melkist.utils.SEEKER_ITEM_TYPE
import com.example.melkist.utils.User.token
import kotlinx.coroutines.launch

class ChooseCatSubCatViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList

    fun getFileCategories(token: String, typeId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e("TAG", "getFileCategoryType: $typeId")
                _itemOptionList.value =
                    Api.retrofitService.getFileCategories(token, typeId).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getFileCategoryType(token: String, typeId: Int, catId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                Log.e("TAG", "getFileCategoryType: $typeId , $catId")
                _itemOptionList.value =
                    Api.retrofitService.getFileCategoryTypes(token, typeId, catId = catId!!).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun emptyList() {
        _itemOptionList.value = listOf()
    }
}
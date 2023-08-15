package com.example.melkist.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.MainActivity
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.EMPTY_CATEGORY_ID
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.internetProblemDialog
import com.example.melkist.utils.isOnline
import kotlinx.coroutines.launch

class ChooseCatSubCatViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    var itemType: Int = 0
    var catId: Int = EMPTY_CATEGORY_ID
    var catTitle: String = ""
    var subCatId: Int = EMPTY_CATEGORY_ID
    var subCatTitle: String = ""
    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList

    fun getCatArray(): Array<String> {
        return arrayOf(catId.toString(), catTitle)
    }

    fun getSubCatArray(): Array<String> {
        return arrayOf(subCatId.toString(), subCatTitle)
    }

    fun getFileCategories(activity: Activity, token: String, typeId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFileCategories(activity, token, typeId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    Log.e("TAG", "getFileCategoryType: $typeId")
                    _itemOptionList.value =
                        Api.retrofitService.getFileCategories(token, typeId).data!!
                    Log.e("TAG", "getFileCategories: ${_itemOptionList.value.toString()} ")
                    _itemOptionList.value?.apply {
                        if (isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    if (activity is MainActivity)
                        handleSystemException(viewModelScope, "${activity.user?.id}, ChooseCatSubCatViewModel, getFileCategories, ", e)
                    else
                        handleSystemException(viewModelScope, "ChooseCatSubCatViewModel, getFileCategories, ", e)
                }
            }
    }

    fun getFileCategoryType(activity: Activity, token: String, typeId: Int, catId: Int) {
        if (!isOnline(activity))
            internetProblemDialog(activity) { _, _ ->
                getFileCategoryType(activity, token, typeId, catId)
            }
        else
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    Log.e("TAG", "getFileCategoryType: $typeId , $catId")
                    _itemOptionList.value =
                        Api.retrofitService.getFileCategoryTypes(
                            token,
                            typeId,
                            catId = catId!!
                        ).data!!
                    Log.e("TAG", "getFileCategoryType: ${_itemOptionList.value.toString()} ")
                    _itemOptionList.value?.apply {
                        if (isEmpty()) _status.value = ApiStatus.NO_DATA
                        else _status.value = ApiStatus.DONE
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    if (activity is MainActivity)
                        handleSystemException(viewModelScope, "${activity.user?.id}, ChooseCatSubCatViewModel, getFileCategoryType, ", e)
                    else
                        handleSystemException(viewModelScope, "ChooseCatSubCatViewModel, getFileCategoryType, ", e)
                }
            }
    }

    fun emptyList() {
        _itemOptionList.value = listOf()
    }
}
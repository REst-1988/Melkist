package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.melkist.models.PcrsData
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.ItemType

class AddItemViewModel: ViewModel() {

    enum class ItemType {SEEKER, OWNER}

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _itemOptionList = MutableLiveData<List<PcrsData>>()
    val itemOptionList: LiveData<List<PcrsData>> = _itemOptionList

    private var itemType: ItemType = ItemType.SEEKER

    fun getItemType() = itemType
    fun setItemType(type: ItemType) { itemType = type }

}
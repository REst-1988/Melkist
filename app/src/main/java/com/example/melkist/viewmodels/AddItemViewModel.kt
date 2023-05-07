package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.FileTypes
import com.example.melkist.models.PcrsData
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.ItemType
import kotlinx.coroutines.launch

class AddItemViewModel : ViewModel() {

    enum class ItemType { CHOOSE, SEEKER, OWNER }
    enum class ReqSource {CATEGORY, SUB_CATEGORY}

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList

    private var itemType: ItemType = ItemType.CHOOSE
    fun getItemType() = itemType
    fun setItemType(type: ItemType) {
        itemType = type
    }

    private var reqSource: ReqSource = ReqSource.CATEGORY
    fun getReqSource() = reqSource
    fun setReqSource(reqSource: ReqSource) {
        this.reqSource = reqSource
    }

    private var typeId: Int = 0
    private var typeTitle: String = ""
    var catId: Int = 0
    var catTitle: String = ""
    var cityId: Int = 0
    var cityTitle: String = ""
    var regionId: Int = 0
    var regionTitle: String = ""
    var region2Id: Int? = null
    var region2Title: String? = null
    var region3Id: Int? = null
    var region3Title: String? = null

    fun getTypeId(): Int {
        val fileTypes = FileTypes()
        typeId = if (getItemType() == ItemType.SEEKER)
            fileTypes.seeker.id
        else if (getItemType() == ItemType.OWNER)
            fileTypes.owner.id
        else
            -1
        return typeId
    }
    fun getTypeTitle(): String {
        val fileTypes = FileTypes()
        typeTitle = if (getItemType() == ItemType.SEEKER)
            fileTypes.seeker.title
        else if (getItemType() == ItemType.OWNER)
            fileTypes.owner.title
        else
            ""
        return typeTitle
    }

    fun getFileCategories(){
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _itemOptionList.value =
                    Api.retrofitService.getFileCategories(getTypeId()).data!!
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getFileCategoryType() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _itemOptionList.value =
                    Api.retrofitService.getFileCategoryTypes(getTypeId(), catId = catId).data!!
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
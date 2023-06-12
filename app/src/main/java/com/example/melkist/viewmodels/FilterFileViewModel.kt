package com.example.melkist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.FileResponse
import com.example.melkist.models.FileTypes
import com.example.melkist.models.PcrsData
import com.example.melkist.models.RegionResponseData

class FilterFileViewModel: ViewModel() {


    private val _itemOptionList = MutableLiveData<List<CatSubCatModel>>()
    val itemOptionList: LiveData<List<CatSubCatModel>> = _itemOptionList
    private val _pcrsList = MutableLiveData<List<PcrsData>>()
    val pcrsList: LiveData<List<PcrsData>> = _pcrsList
    private val _regionList = MutableLiveData<List<RegionResponseData>>()
    val regionList: LiveData<List<RegionResponseData>> = _regionList

    private val _filesResponse = MutableLiveData <FileResponse>()
    val filesResponse: LiveData<FileResponse> = _filesResponse


    var catId: Int = 0
    var catTitle: String = ""
    var subCatId: Int = 0
    var subCatTitle: String = ""

    private var itemType: MapViewModel.ItemType = MapViewModel.ItemType.SHOW_ALL
    fun getItemType() = itemType
    fun setItemType(type: MapViewModel.ItemType) {
        itemType = type
    }

    fun getTypeId(requestCode: MapViewModel.ItemType): Int {
        val fileTypes = FileTypes()
        return when (requestCode) {
            MapViewModel.ItemType.SHOW_SEEKER -> fileTypes.seeker.id
            MapViewModel.ItemType.SHOW_OWNER -> fileTypes.owner.id
            else -> -1
        }
    }

    fun resetCatSubCat() {
        catId = 0
        catTitle = ""
        subCatId = 0
        subCatTitle = ""
    }

    fun resetSubCat() {
        subCatId = 0
        subCatTitle = ""
    }
}
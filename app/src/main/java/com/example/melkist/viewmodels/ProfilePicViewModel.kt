package com.example.melkist.viewmodels

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.network.Api
import com.example.melkist.utils.ApiStatus
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProfilePicViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus> = _status
    private val _uploadResponse = MutableLiveData<PublicResponseModel>()
    val uploadResponse: LiveData<PublicResponseModel> = _uploadResponse


    var isFirstTime: Boolean? = null
    var userId: Int? = null
    var token: String? = null

    var imgUser: Bitmap? = null

    fun getEncodedImage(imgUser: Bitmap) : String?{
        val main = ThumbnailUtils.extractThumbnail(imgUser, 300, 300)
        val byteArrayOutputStreamMain = ByteArrayOutputStream()
        main.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamMain)
        return Base64.encodeToString(byteArrayOutputStreamMain.toByteArray(), Base64.DEFAULT)
    }

    fun uploadProfilePic () {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _uploadResponse.value =
                    Api.retrofitService.uploadProfilePic(token!!, userId!!, getEncodedImage(imgUser!!)!!)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }
}
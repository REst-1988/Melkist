package com.example.melkist.adapters.bindingadapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.adapters.FavListAdapter
import com.example.melkist.adapters.MyFilesAdapter
import com.example.melkist.adapters.TeamMemberAdapter
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.Fav
import com.example.melkist.models.FileData
import com.example.melkist.models.Location
import com.example.melkist.models.PcrsData
import com.example.melkist.models.User
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getTimeStampForLoadImages
import java.io.File

@BindingAdapter("imgUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url?.let {
        val a = "$url?d=${getTimeStampForLoadImages()}"
        val imgUri = a.toUri().buildUpon().scheme("https").build() // TODO: CHECK if https or htt
        imageView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<PcrsData>?
) {
    val adapter = recyclerView.adapter as ChoosingPcrsAdapter
    adapter.submitList(data)
}

@BindingAdapter("favListData")
fun bindFavRecyclerView(
    recyclerView: RecyclerView,
    data: List<Fav>?
) {
    val adapter = recyclerView.adapter as FavListAdapter
    adapter.submitList(data)
}

@BindingAdapter("teamMemberData")
fun bindTeamMemberData(
    recyclerView: RecyclerView,
    data: List<User>?
) {
    val adapter = recyclerView.adapter as TeamMemberAdapter
    adapter.submitList(data)
}

@BindingAdapter("myFilesData")
fun bindMyFilesData(
    recyclerView: RecyclerView,
    data: List<FileData>?
) {
    val adapter = recyclerView.adapter as MyFilesAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataCatSubCat")
fun bindRecyclerViewCatSubCat(
    recyclerView: RecyclerView,
    data: List<CatSubCatModel>?
) {
    Log.e("TAG", "bindRecyclerViewCatSubCat: TEST ${data?.size} ", )
    val adapter = recyclerView.adapter as ChoosingCatSubCatAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindingCrashData")
fun bindingCrashData (view: View?, status: ApiStatus){
    view?.let {
        when (status) {
            ApiStatus.ERROR -> it.visibility = View.VISIBLE
            else -> it.visibility = View.GONE
        }
    }
}

@BindingAdapter("bindLoading")
fun bindLoading(view: View?, status: ApiStatus) {
    view?.let {
        when (status) {
            ApiStatus.LOADING -> it.visibility = View.INVISIBLE
            else -> it.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("bindProgressLoading")
fun bindProgressLoading(progressBar: ProgressBar?, status: ApiStatus) {
    progressBar?.let {
        when (status) {
            ApiStatus.LOADING -> it.visibility = View.VISIBLE
            else -> it.visibility = View.GONE
        }
    }
}

@BindingAdapter("setVisibility")
fun bindVisibility(view: View?, isVisible: Boolean) {
    view?.let {
        if (isVisible)
            it.visibility = View.VISIBLE
        else
            it.visibility = View.GONE
    }
}

@BindingAdapter(value = ["bindList", "bindStatus"])
fun bindNoData(view: View?, list: List<Any>?, status: ApiStatus){
    view?.let {
        if (status == ApiStatus.DONE && list.isNullOrEmpty())
            it.visibility = View.VISIBLE
        else
            it.visibility = View.GONE
    }
}
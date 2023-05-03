package com.example.melkist.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.models.PcrsData
import com.example.melkist.utils.ApiStatus

@BindingAdapter("imgUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url?.let {
        val imgUri = url.toUri().buildUpon().scheme("https").build() // TODO: CHECK if https or http
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
package com.example.melkist.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.models.FileDataResponse
import com.example.melkist.utils.showToast
import com.example.melkist.views.map.MapP1Frag

class ImagePagerAdapter(
    private val fragment: MapP1Frag?,
    private val file: FileDataResponse
): RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_image_view_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        file.data!!.images?.apply {
            bindImage(holder.imageView, this[position])
        }
        holder.itemView.setOnClickListener {
            fragment?.onMoreDetailFileClick()
        }
    }

    override fun getItemCount(): Int {
        return file.data!!.images!!.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img)
    }
}
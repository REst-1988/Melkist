package com.example.melkist.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.models.FileAllDataResponse

class ImageAdapter(
    private val file: FileAllDataResponse
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_image_view_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var imgUri: Uri? = null
        file.data!!.extraInfo?.apply {
            imgUri = this[position]!!.toUri().buildUpon().scheme("https").build() // TODO: CHECK if https or http
        }
        holder.imageView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }

    override fun getItemCount(): Int {
        return file.data!!.extraInfo!!.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img)
    }
}
package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.models.FileDataResponse
import com.example.melkist.views.map.MapP1Frag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImagePagerAdapter(
    private val fragment: MapP1Frag?,
    private val bottomSheet: BottomSheetDialogFragment?,
    private val images: List<String>?
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_image_view_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        images?.apply {
            bindImage(holder.imageView, this[position])
        }
        holder.itemView.setOnClickListener {
            bottomSheet?.apply {
                fragment?.onMoreDetailFileClick(this)
            }
        }
    }

    override fun getItemCount(): Int {
        return images?.size ?: 0
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img)
    }
}
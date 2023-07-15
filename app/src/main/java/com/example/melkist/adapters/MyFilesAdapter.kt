package com.example.melkist.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.databinding.LayoutItemListMyFilesBinding
import com.example.melkist.models.FileData
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.utils.formatNumber
import com.example.melkist.views.profile.ProfileMyFilesFrag

class MyFilesAdapter(private val fragment: Fragment) :
    ListAdapter<FileData, MyFilesAdapter.MyFilesViewHolder>(DiffUtilCallBack) {

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<FileData>() {
        override fun areItemsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem.created_at == newItem.created_at
        }
    }

    class MyFilesViewHolder(
        private val context: Context,
        private var binding: LayoutItemListMyFilesBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FileData) {
            data.typeInfo?.apply {
                if (fileType!!.id == FileTypes().seeker.id)
                    bindSeeker(data)
                else
                    bindingOwner(data)
            }
        }

        private fun bindSeeker(data: FileData) {
            Log.e("TAG", "bind 1 data = $data")
            binding.apply {
                layoutSeeker.visibility = View.VISIBLE
                layoutOwner.visibility = View.GONE
                ////////////
                txtSubCatSeeker.text = data.typeInfo!!.subCategory!!.title
                txtCatSeeker.text = data.typeInfo.category!!.title
                txtRegionSeeker.text = data.locations.getLocationTexts()
                txtPriceFromSeeker.text = data.price.from.getPriceText()
                txtPriceToSeeker.text = data.price.to.getPriceText()
            }
        }

        private fun bindingOwner(data: FileData) {
            Log.e("TAG", "bind 2 data = $data")
            binding.apply {
                layoutSeeker.visibility = View.GONE
                layoutOwner.visibility = View.VISIBLE
                /////////////
                txtSubCatOwner.text = data.typeInfo!!.subCategory!!.title
                txtCatOwner.text = data.typeInfo.category!!.title
                txtRegionOwner.text = data.locations[0].region.title
                txtRoomNoOwner.text =
                    context.resources.getString(R.string.rooms, data.roomNo.from.toString())
                txtPriceOwner.text = context.resources.getString(
                    R.string.price_with_variable,
                    formatNumber(data.price.from!!.toDouble())
                )
                data.images?.let {
                    val imgUri = it[0].toUri().buildUpon().scheme("https").build()

                    Log.e("TAG", "bindingOwner: test $imgUri", )
                    imgMainOwner.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                }
            }
        }

        private fun List<Location>.getLocationTexts(): String {
            var a = ""
            this.forEach {
                a += it.region.title + "  "
            }
            return a
        }

        private fun Long?.getPriceText(): String {
            return if (this != null)
                formatNumber(this.toDouble())
            else
                context.resources.getString(R.string.dots)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFilesViewHolder {
        return MyFilesViewHolder(
            fragment.requireContext(),
            LayoutItemListMyFilesBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: MyFilesViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file)
        holder.itemView.setOnClickListener {
            (fragment as ProfileMyFilesFrag).choosingItemAction(file)
        }
    }
}
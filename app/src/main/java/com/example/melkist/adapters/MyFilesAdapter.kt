package com.example.melkist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.LayoutItemListMyFilesBinding
import com.example.melkist.models.FileData
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.views.profile.ProfileMyFilesFrag
import com.example.melkist.views.profile.ai.ProfileAiSuggestionFilterFrag

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

        fun isNeedToAddToFilterFileList(): Boolean {
            if (binding.chooseCurtain.visibility == View.GONE) {
                binding.chooseCurtain.visibility = View.VISIBLE
                return true
            }
            binding.chooseCurtain.visibility = View.GONE
            return false
        }

        fun checkFilterItems(list: MutableList<Int>, id: Int) {
            if (list.contains(id))
                binding.chooseCurtain.visibility = View.VISIBLE
            else
                binding.chooseCurtain.visibility = View.GONE
        }

        private fun bindSeeker(data: FileData) {
            binding.apply {
                layoutSeeker.visibility = View.VISIBLE
                layoutOwner.visibility = View.GONE
                ////////////
                txtSubCatSeeker.text = data.typeInfo!!.subCategory!!.title
                txtCatSeeker.text = data.typeInfo.category!!.title
                txtRegionSeeker.text = data.locations.getLocationTexts()
                txtPriceSeeker.text = getPropertyPeriodsPriceText(
                    context,
                    data.price,
                    R.string.price,
                    R.string.tooman
                )
            }
        }

        private fun bindingOwner(data: FileData) {
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
                    formatNumber((data.price.from?:0L))// TODO: change this data might be different after changing in database
                )
                data.images?.let {
                    if (it.isNotEmpty())
                        bindImage(imgMainOwner, it[0])
                    else imgMainOwner.setImageDrawable(null)
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
                formatNumber(this.toLong())
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
        if (fragment is ProfileAiSuggestionFilterFrag && fragment.getFilterList().isNotEmpty())
            holder.checkFilterItems(fragment.getFilterList(), file.id)
        holder.itemView.setOnClickListener {
            when (fragment) {
                is ProfileMyFilesFrag -> fragment.choosingItemAction(file)
                is ProfileAiSuggestionFilterFrag -> {
                    fragment.choosingItem(file, holder.isNeedToAddToFilterFileList())
                }
            }
        }
    }
}
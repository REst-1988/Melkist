package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.*
import com.example.melkist.databinding.ItemListMultiTextBinding
import com.example.melkist.models.RegionResponseData
import com.example.melkist.utils.concatenateText
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.views.add.AddP5CrFrag

class ChoosingRegionAdapter(val viewModel: ViewModel, val fragment: Fragment):
    ListAdapter<RegionResponseData, ChoosingRegionAdapter.RegionViewHolder>(DiffUtilCallBack),
    Filterable{
    var mListRef: List<RegionResponseData>? = null
    var mFilteredList: List<RegionResponseData>? = null
    private var isFilter = false

    init {
        isFilter = false
    }

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<RegionResponseData>() {
        override fun areItemsTheSame(oldItem: RegionResponseData, newItem: RegionResponseData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegionResponseData, newItem: RegionResponseData): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun submitList(list: List<RegionResponseData>?) {
        super.submitList(list)
        if (!isFilter)
            mListRef = list
    }

    class RegionViewHolder(private var binding: ItemListMultiTextBinding):
        ViewHolder(binding.root){
            fun bind(data: RegionResponseData){
                binding.txt.text = data.title
                if (data.tags!!.isNotEmpty()) {
                    binding.txtOther.visibility = VISIBLE
                    binding.txtOther.text = concatenateText(data.tags)
                } else
                    binding.txtOther.visibility = GONE
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemListMultiTextBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val rc = getItem(position)
        holder.bind(rc)
        holder.itemView.setOnClickListener{
            (viewModel as AddItemViewModel).choosingItemActionRegion(rc)
            (fragment as AddP5CrFrag).back()
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isNotEmpty()  && charString != "") {
                    mListRef!!
                        .filter {
                            it.title.contains(charString) ||
                                    concatenateText(it.tags).contains(charString)
                        }
                }else{
                    mListRef
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                try {
                    mFilteredList = filterResults.values as List<RegionResponseData>?
                    isFilter = true
                    submitList(mFilteredList)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}
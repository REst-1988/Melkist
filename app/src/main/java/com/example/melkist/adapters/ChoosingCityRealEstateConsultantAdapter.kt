package com.example.melkist.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListSingleTextBinding
import com.example.melkist.models.Data
import java.util.*
import kotlin.collections.ArrayList


class ChoosingCityRealEstateConsultantAdapter :
    ListAdapter<Data, ChoosingCityRealEstateConsultantAdapter.ProvinceCityViewHolder>(
        DiffUtilCallBack
    ), Filterable {

    var mListRef: List<Data>? = null
    var mFilteredList: List<Data>? = null

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class ProvinceCityViewHolder(private var binding: ItemListSingleTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            binding.txt.text = data.title
           // binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceCityViewHolder {
        return ProvinceCityViewHolder(
            ItemListSingleTextBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ProvinceCityViewHolder, position: Int) {
        val provinceCity = getItem(position)
        holder.bind(provinceCity)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredList = mListRef
                } else {
                    mListRef?.let {
                        val filteredList = arrayListOf<Data>()
/*                        for (baseDataItem in mListRef!!) {
                            if (baseDataItem is BaseDataItem.DataItemWrapper) {
                                if (charString.toLowerCase(Locale.ENGLISH) in baseDataItem.dataItem.Name.toLowerCase(
                                        Locale.ENGLISH
                                    )
                                ) filteredList.add(baseDataItem)
                            }
                        }*/
                        mFilteredList = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
/*                mFilteredList = filterResults.values as ArrayList<BaseDataItem>
                submitList(mFilteredList)*/
            }
        }
    }
}
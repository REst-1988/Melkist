package com.example.melkist.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListSingleTextBinding
import com.example.melkist.models.PcrsData
import com.example.melkist.viewmodels.SignupViewModel
import com.example.melkist.views.login.signup.SignupP4ChoosingPcrsFrag
import java.util.*
import kotlin.collections.ArrayList

class ChoosingPcrsAdapter (val viewModel: SignupViewModel, val fragment: Fragment):
    ListAdapter<PcrsData, ChoosingPcrsAdapter.PcrsViewHolder>(DiffUtilCallBack),
    Filterable {

    var mListRef: List<PcrsData>? = null
    var mFilteredList: List<PcrsData>? = null
    private var isFilter = false

    init {
        isFilter = false
    }


    companion object DiffUtilCallBack : DiffUtil.ItemCallback<PcrsData>() {
        override fun areItemsTheSame(oldItem: PcrsData, newItem: PcrsData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PcrsData, newItem: PcrsData): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun submitList(list: List<PcrsData>?) {
        super.submitList(list)
        if (!isFilter)
            mListRef = list
    }

    class PcrsViewHolder(private var binding: ItemListSingleTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PcrsData) {
            if(data.user != null) {
                binding.txt.text = String.format("%s (%s %s)", data.title, data.user!!.name, data.user!!.family)
            }else
                binding.txt.text = data.title
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcrsViewHolder {
        return PcrsViewHolder(
            ItemListSingleTextBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PcrsViewHolder, position: Int) {
        val pcrs = getItem(position)
        holder.bind(pcrs)
        holder.itemView.setOnClickListener{
            choosingItemAction(pcrs)
            (fragment as SignupP4ChoosingPcrsFrag).back()
        }
    }

    private fun choosingItemAction(pcrs: PcrsData){
        when (viewModel.PcrsCondition) {
            SignupViewModel.Pcrs.PROVINCE -> {
                viewModel.provinceId = pcrs.id!!
                viewModel.provinceTitle = pcrs.title!!
            }
            SignupViewModel.Pcrs.CITY -> {
                viewModel.cityId = pcrs.id!!
                viewModel.cityTitle = pcrs.title!!
            }
            SignupViewModel.Pcrs.REAL_ESTATE -> {
                viewModel.realEstateId = pcrs.id!!
                viewModel.realEstateTitle = pcrs.title!!
                Log.e("TAG", "choosingItemAction: ${pcrs.user?.id}" )
                viewModel.parentId = pcrs.user?.id
            }
            else -> {
                viewModel.supervisorId = pcrs.id!!
                viewModel.supervisorTitle = pcrs.title!!
                viewModel.parentId = pcrs.id
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                Log.e("TAG", "performFiltering: ${mListRef?.size}", )
                if (charString.isEmpty() || charString == "") {
                    mFilteredList = mListRef
                    isFilter = false
                } else {
                    mListRef?.let {
                        val filteredList = arrayListOf<PcrsData>()
                        for (item in mListRef!!) {
                            if (item.title!!.contains(charString)) {
                                filteredList.add(item)
                            }
                        }
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
                mFilteredList = filterResults.values as List<PcrsData>?
                isFilter = true
                submitList(mFilteredList)
            }
        }
    }
}
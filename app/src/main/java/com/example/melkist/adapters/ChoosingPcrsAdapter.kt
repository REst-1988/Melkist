package com.example.melkist.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListSingleTextBinding
import com.example.melkist.models.PcrsData
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.viewmodels.SignupViewModel
import com.example.melkist.views.add.AddP5CrFrag
import com.example.melkist.views.login.signup.SignupP4ChoosingPcrsFrag

/**
 * this adapter used for to different view model so every action handled on its own view model
 */
class ChoosingPcrsAdapter(val viewModel: ViewModel, val fragment: Fragment) :
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
            if (data.user != null) {
                binding.txt.text =
                    String.format("%s (%s %s)", data.title, data.user!!.firstName, data.user!!.lastName)
            } else
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
        holder.itemView.setOnClickListener {
            if (viewModel is AddItemViewModel) {
                Log.e("TAG", "onBindViewHolder: aaa test ", )
                viewModel.choosingItemActionPc(pcrs)
                (fragment as AddP5CrFrag).back()
            } else {
                Log.e("TAG", "onBindViewHolder: aaa test 2" , )
                (viewModel as SignupViewModel).choosingItemAction(pcrs)
                (fragment as SignupP4ChoosingPcrsFrag).back()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isNotEmpty()  && charString != "") {
                    mListRef!!
                        .filter {
                            it.title!!.contains(charString)
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
                mFilteredList = filterResults.values as List<PcrsData>?
                isFilter = true
                submitList(mFilteredList)
            }
        }
    }
}
package com.example.melkist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.databinding.ItemListFavBinding
import com.example.melkist.models.Fav
import com.example.melkist.models.Period
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showToast

class FavListAdapter(val viewModel: ViewModel, val fragment: Fragment) :
    ListAdapter<Fav, FavListAdapter.FavListViewHolder>(DiffUtilCallBack), Filterable {

    var mListRef: List<Fav>? = null
    var mFilteredList: List<Fav>? = null
    private var isFilter = false

    init {
        isFilter = false
    }

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<Fav>() {
        override fun areItemsTheSame(oldItem: Fav, newItem: Fav): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fav, newItem: Fav): Boolean {
            return oldItem.user.id == newItem.user.id
        }
    }

    override fun submitList(list: List<Fav>?) {
        super.submitList(list)
        if (!isFilter) mListRef = list
    }

    class FavListViewHolder(private var binding: ItemListFavBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Fav) {
            binding.txtRegion.text = data.locations[0].region.title
            binding.txtRoomNo.text = getPropertyPeriodsText(data.roomNo, R.string.room)
            binding.txtSize.text = getPropertyPeriodsText(data.size, R.string.size_squere_meter)
            binding.txtPrice.text = getPropertyPeriodsText(data.price, R.string.tooman)
            binding.txtRealEstate.text = String.format("%s %s", context.resources.getString(R.string.real_estate_title), data.user.realEstate)
            binding.txtAgent.text = String.format("%s %s", data.user.firstName, data.user.lastName)
            data.user.profilePic?.let {
                val imgUri = it.toUri().buildUpon().scheme("https").build()
                binding.imgProfile.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
            data.image?.let {
                val imgUri = it.toUri().buildUpon().scheme("https").build()
                binding.imgMain.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
            binding.executePendingBindings()
        }

        private fun getPropertyPeriodsText(period: Period, @StringRes unit: Int): String {
            val conjunctions = if (period.to == period.from) ""
            else context.resources.getString(
                R.string.to
            ) + " " + period.to + " " + context.resources.getString(
                unit
            )
            return String.format("%s %s", period.from, conjunctions)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListViewHolder {
        return FavListViewHolder(ItemListFavBinding.inflate(LayoutInflater.from(parent.context)), fragment.requireContext())
    }

    override fun onBindViewHolder(holder: FavListViewHolder, position: Int) {
        val fav = getItem(position)
        holder.bind(fav)
        holder.itemView.setOnClickListener{
            choosingItemAction(fav)
        }
    }

    private fun choosingItemAction(fav: Fav) {
        //TODO: change this to real choose option
        showToast(fragment.requireContext(), "item ${fav.id} clicked!")
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isNotEmpty()  && charString != "") {
                    mListRef!!
                        .filter {
                            it.locations[0].region.title.contains(charString) ||
                                    concatenateText(it.locations[0].region.tags).contains(charString) ||
                                    it.locations[1].region.title.contains(charString) ||
                                    concatenateText(it.locations[1].region.tags).contains(charString) ||
                                    it.locations[2].region.title.contains(charString) ||
                                    concatenateText(it.locations[2].region.tags).contains(charString)
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
                    mFilteredList = filterResults.values as List<Fav>?
                    isFilter = true
                    submitList(mFilteredList)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

}
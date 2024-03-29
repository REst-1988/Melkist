package com.example.melkist.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.ItemListFavBinding
import com.example.melkist.models.Fav
import com.example.melkist.models.FileTypes
import com.example.melkist.views.fav.FavListFrag

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
            binding.apply {
                txtCat.text = data.typeInfo?.category?.title
                txtSubCat.text = data.typeInfo?.subCategory?.title
                txtRegion.text = data.locations[0].region.title
                //txtSize.text = ""
                // getPropertyPeriodsText(context, data.size, R.string.meterage, R.string.squere_meter)
                //txtPrice.text = ""
                //getPropertyPeriodsPriceText(context, data.price, R.string.price, R.string.tooman)
                txtRealEstate.text = String.format(
                    "%s %s",
                    context.resources.getString(R.string.real_estate_title),
                    data.user.realEstate
                )
                txtAgent.text = String.format("%s %s", data.user.firstName, data.user.lastName)
                if (data.user.profilePic.isNullOrEmpty()) imgProfile.setImageResource(R.drawable.logo_color)
                else bindImage(imgProfile, data.user.profilePic)

                Log.e("TAG", "bind: ${data.image.isNullOrEmpty()},  ${data.image}")
                if ((data.typeInfo?.fileType?.id ?: 1) == FileTypes().seeker.id) {
                    cardImgOwner.visibility = View.GONE
                    cardImgSeeker.visibility = View.VISIBLE
                } else {
                    cardImgOwner.visibility = View.VISIBLE
                    cardImgSeeker.visibility = View.GONE
                    bindImage(imgMainOwner, data.image)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListViewHolder {
        return FavListViewHolder(
            ItemListFavBinding.inflate(LayoutInflater.from(parent.context)),
            fragment.requireContext()
        )
    }

    override fun onBindViewHolder(holder: FavListViewHolder, position: Int) {
        val fav = getItem(position)
        holder.bind(fav)
        holder.itemView.setOnClickListener {
            (fragment as FavListFrag).choosingItemAction(fav)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                Log.e("TAG", "performFiltering: $charString")
                mFilteredList = if (charString.isNotEmpty() && charString != "") {
                    mListRef!!
                        .filter {
                            it.locations[0].region.title.contains(charString)/* ||
                                    concatenateText(it.locations[0].region.tags).contains(charString)*/
                        }
                } else {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
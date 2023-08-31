package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListPropertyIntroBinding
import com.example.melkist.databinding.ItemListTeamMembersBinding
import com.example.melkist.models.SuggestionModel
import com.example.melkist.models.User
import com.example.melkist.views.profile.ProfileManageTeamFrag

class SuggestionAdapter(private val fragment: Fragment) :
    ListAdapter<SuggestionModel, SuggestionAdapter.SuggestionViewHolder>(DiffUtilCallBack)/*, Filterable*/ {

/*    var mListRef: List<User>? = null
    var mFilteredList: List<User>? = null
    private var isFilter = false

    init {
        isFilter = false
    }*/

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<SuggestionModel>() {
        override fun areItemsTheSame(oldItem: SuggestionModel, newItem: SuggestionModel): Boolean {
            return TODO()//.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SuggestionModel, newItem: SuggestionModel): Boolean {
            return TODO()//.mobile == newItem.mobile
        }
    }

/*    override fun submitList(list: List<User>?) {
        super.submitList(list)
        if (!isFilter) mListRef = list
    }*/

    class SuggestionViewHolder(private val binding: ItemListPropertyIntroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SuggestionModel) {
            //binding.user = data
            //binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(ItemListPropertyIntroBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.bind(suggestion)
/*        holder.itemView.setOnClickListener{
            (fragment as ProfileManageTeamFrag).choosingItemAction(user)
        }*/
    }
}
package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListTeamMembersBinding
import com.example.melkist.models.User
import com.example.melkist.views.profile.ProfileManageTeamFrag

class TeamMemberAdapter(private val fragment: Fragment) :
    ListAdapter<User, TeamMemberAdapter.UserListViewHolder>(DiffUtilCallBack)/*, Filterable*/ {

/*    var mListRef: List<User>? = null
    var mFilteredList: List<User>? = null
    private var isFilter = false

    init {
        isFilter = false
    }*/

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.mobile == newItem.mobile
        }
    }

/*    override fun submitList(list: List<User>?) {
        super.submitList(list)
        if (!isFilter) mListRef = list
    }*/

    class UserListViewHolder(private var binding: ItemListTeamMembersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            binding.user = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(ItemListTeamMembersBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener{
            (fragment as ProfileManageTeamFrag).choosingItemAction(user)
        }
    }
    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isNotEmpty()  && charString != "") {
                    mListRef!!
                        .filter {

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
                    mFilteredList = filterResults.values as List<User>?
                    isFilter = true
                    submitList(mFilteredList)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }*/
}
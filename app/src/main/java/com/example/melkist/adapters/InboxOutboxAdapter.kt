package com.example.melkist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.ItemListInboxOutboxBinding
import com.example.melkist.models.Status
import com.example.melkist.utils.INBOX
import com.example.melkist.views.profile.alerts.ProfileInboxFrag
import com.example.melkist.views.profile.alerts.ProfileOutboxFrag

class InboxOutboxAdapter(
    private val fragInbox: ProfileInboxFrag?,
    private val fragOutbox: ProfileOutboxFrag?,
    private val inboxOutbox: Int
) : ListAdapter<Status, InboxOutboxAdapter.ViewHolder>(DiffUtilCallBack) {

    // Diff call back does not used in this project yet but it would be handy for future changes
    companion object DiffUtilCallBack : DiffUtil.ItemCallback<Status>() {
        override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean {
            return oldItem.file!!.id == newItem.file!!.id
        }

        override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean {
            return oldItem.requestId == newItem.requestId
        }
    }

    class ViewHolder(
        private val context: Context,
        private val inboxOutboxBinding: ItemListInboxOutboxBinding,
        private val inboxOutbox: Int
    ) : RecyclerView.ViewHolder(inboxOutboxBinding.root) {
        fun bind(data: Status) {
            if (inboxOutbox == INBOX) {
                inboxOutboxBinding.mainLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
                bindInbox(data)
            } else {
                inboxOutboxBinding.mainLayout.layoutDirection = View.LAYOUT_DIRECTION_LTR
                bindOutbox(data)
            }
        }

        private fun bindInbox(data: Status) {
            data.file?.images?.apply {
                bindImage(inboxOutboxBinding.imgMain, this[0])
            }
            data.targetUser?.profilePic?.apply {
                bindImage(inboxOutboxBinding.imgProfile, this)
            }
            inboxOutboxBinding.txtTitle.text = context.resources.getString(
                R.string.cooperation_request_inbox
            )
            data.targetUser?.apply {
                inboxOutboxBinding.txtApplicant.text = context.resources.getString(
                    R.string.applicant_inbox, this.firstName, this.lastName, this.realEstate
                )
            }
            data.file?.locations?.apply {
                inboxOutboxBinding.txtRegion.text = context.resources.getString(
                    R.string.region_outbox_outbox, this[0].region.title
                )
            }
        }

        private fun bindOutbox(data: Status) {
            data.file?.images?.apply {
                if (this.isNotEmpty()) bindImage(inboxOutboxBinding.imgMain, this[0])
            }
            data.targetUser?.profilePic?.apply {
                bindImage(inboxOutboxBinding.imgProfile, this)
            }
            inboxOutboxBinding.txtTitle.text = context.resources.getString(
                R.string.cooperation_request_outbox
            )
            data.targetUser?.apply {
                inboxOutboxBinding.txtApplicant.text = context.resources.getString(
                    R.string.applicant_outbox, this.firstName, this.lastName, this.realEstate
                )
            }
            data.file?.locations?.apply {
                inboxOutboxBinding.txtRegion.text = context.resources.getString(
                    R.string.region_outbox_outbox, this[0].region.title
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.context,
            ItemListInboxOutboxBinding.inflate(LayoutInflater.from(parent.context)),
            inboxOutbox
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (inboxOutbox == INBOX) {
                fragInbox?.showInboxDialog(item)
            } else {
                fragOutbox?.showOutboxDialog(item, currentList.filter { it.requestId == item.requestId })
            }
        }
    }


}
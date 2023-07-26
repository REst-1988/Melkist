package com.example.melkist.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.DialogLayoutInboxOutboxBinding
import com.example.melkist.databinding.ItemListInboxOutboxBinding
import com.example.melkist.databinding.ItemListSendReceivedCooperationBinding
import com.example.melkist.models.Status
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.RECEIVED

class SendReceivedCooperationAdapter(private val context: Context, private val sendReceived: Int) :
    ListAdapter<Status, SendReceivedCooperationAdapter.ViewHolder>(DiffUtilCallBack) {

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
        private val sendReceivedBinding: ItemListSendReceivedCooperationBinding,
        private val sendReceived: Int
    ) : RecyclerView.ViewHolder(sendReceivedBinding.root) {
        fun bind(data: Status) {
            if (sendReceived == RECEIVED) {
                sendReceivedBinding.mainLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
                bindInbox(data)
            } else {
                sendReceivedBinding.mainLayout.layoutDirection = View.LAYOUT_DIRECTION_LTR
                bindOutbox(data)
            }
        }

        private fun bindInbox(data: Status) {
            data.file?.images?.apply {
                if (this.isNotEmpty())
                    bindImage(sendReceivedBinding.imgMain, this[0])
            }
            data.targetUser?.profilePic?.apply {
                bindImage(sendReceivedBinding.imgProfile, this)
            }
            data.targetUser?.apply {
                sendReceivedBinding.txtApplicant.text =
                    context.resources.getString(
                        R.string.applicant,
                        this.firstName,
                        this.lastName,
                        this.realEstate
                    )
            }
            data.file?.locations?.apply {
                sendReceivedBinding.txtRegion.text =
                    context.resources.getString(
                        R.string.region_outbox_outbox,
                        this[0].region.title
                    )
            }
        }

        private fun bindOutbox(data: Status) {
            data.file?.images?.apply {
                if (this.isNotEmpty())
                    bindImage(sendReceivedBinding.imgMain, this[0])
            }
            data.targetUser?.profilePic?.apply {
                bindImage(sendReceivedBinding.imgProfile, this)
            }
            sendReceivedBinding.txtTitle.text =
                context.resources.getString(
                    R.string.cooperation_request_outbox
                )
            data.targetUser?.apply {
                sendReceivedBinding.txtApplicant.text =
                    context.resources.getString(
                        R.string.applicant_outbox,
                        this.firstName,
                        this.lastName,
                        this.realEstate
                    )
            }
            data.file?.locations?.apply {
                sendReceivedBinding.txtRegion.text =
                    context.resources.getString(
                        R.string.region_outbox_outbox,
                        this[0].region.title
                    )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.context,
            ItemListSendReceivedCooperationBinding.inflate(LayoutInflater.from(parent.context)),
            sendReceived
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (sendReceived == INBOX){
                showInboxDialog(item)
            }else{
                showOutboxDialog(item)
            }
        }
    }

    private fun showInboxDialog(item: Status) {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutInboxOutboxBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
    }


    private fun showOutboxDialog(item: Status) {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutInboxOutboxBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}
package com.example.melkist.adapters

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.databinding.DialogLayoutGetAddDetailsBinding
import com.example.melkist.databinding.DialogLayoutInboxOutboxBinding
import com.example.melkist.databinding.ItemListInboxOutboxBinding
import com.example.melkist.models.Status
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.numInLetter
import com.example.melkist.utils.showToast
import java.lang.Exception
import java.math.BigDecimal

class InboxOutboxAdapter(private val context: Context, private val inboxOutbox: Int) :
    ListAdapter<Status, InboxOutboxAdapter.ViewHolder>(DiffUtilCallBack) {

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
            val imgUri = data.file!!.images!![0].toUri().buildUpon().scheme("https").build()
            Log.e("TAG", "bind 1: $imgUri", )
            inboxOutboxBinding.imgMain.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            val userImgUrl =
                data.targetUser!!.profilePic?.toUri()?.buildUpon()?.scheme("https")?.build()
            Log.e("TAG", "bind 1: $userImgUrl", )
            inboxOutboxBinding.imgProfile.load(userImgUrl) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            inboxOutboxBinding.txtTitle.text =
                context.resources.getString(
                    R.string.cooperation_request_inbox
                )
            inboxOutboxBinding.txtApplicant.text =
                context.resources.getString(
                    R.string.applicant_inbox,
                    data.targetUser.firstName,
                    data.targetUser.lastName,
                    data.targetUser.realEstate
                )
            inboxOutboxBinding.txtRegion.text =
                context.resources.getString(
                    R.string.region_outbox_outbox,
                    data.file.locations!![0].region.title
                )
        }

        private fun bindOutbox(data: Status) {
            val imgUri = data.file!!.images!![0].toUri().buildUpon().scheme("https").build()
            Log.e("TAG", "bind2: $imgUri", )
            inboxOutboxBinding.imgMain.load(imgUri) {
                Log.e("TAG", "bind3: $imgUri", )
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            val userImgUrl =
                data.targetUser!!.profilePic?.toUri()?.buildUpon()?.scheme("https")?.build()
            Log.e("TAG", "bind2: $userImgUrl", )
            inboxOutboxBinding.imgProfile.load(userImgUrl) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            inboxOutboxBinding.txtTitle.text =
                context.resources.getString(
                    R.string.cooperation_request_outbox
                )
            inboxOutboxBinding.txtApplicant.text =
                context.resources.getString(
                    R.string.applicant_outbox,
                    data.targetUser.firstName,
                    data.targetUser.lastName,
                    data.targetUser.realEstate
                )
            try { // TODO: delete this after correction the server by amir
                inboxOutboxBinding.txtRegion.text =
                    context.resources.getString(
                        R.string.region_outbox_outbox,
                        data.file.locations!![0].region.title
                    )
            } catch (e: Exception){
                e.printStackTrace()
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
            if (inboxOutbox == INBOX){
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
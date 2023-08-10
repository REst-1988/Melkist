package com.example.melkist.views.profile.alerts

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.adapters.InboxOutboxAdapter
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.DialogLayoutInboxOutboxBinding
import com.example.melkist.databinding.FragProfileInBoxOutBoxBinding
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Status
import com.example.melkist.utils.OUTBOX
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileOutboxFrag(
    private val fragment: Fragment
) : Fragment() {

    private lateinit var binding: FragProfileInBoxOutBoxBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val statusList: MutableList<Status> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileInBoxOutBoxBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        binding.pullToRefreshMainList.setOnRefreshListener {
            setupOutboxList()
        }
    }

    fun showOutboxDialog(item: Status) {
        val binding = DialogLayoutInboxOutboxBinding.inflate(LayoutInflater.from(context))
        bindOutboxDialogItemViews(binding, item)
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        // on click listeners
        binding.btnCloseDialog.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnMoreDetailDialog.setOnClickListener {
            (activity as MainActivity).user?.apply {
                try {
                    viewModel.getFileInfoById(requireActivity(), token = token!!, item.file!!.id!!, id!!)
                    listenToFileDetailData(alertDialog)
                } catch (e: Exception) {
                    handleSystemException(lifecycleScope, "ProfileOutboxFrag, showOutboxDialog, ", e)
                }
            }
        }
    }

    private fun listenToFileDetailData(alertDialog: AlertDialog) {
        viewModel.fileAllData.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    alertDialog.dismiss()
                    (fragment as ProfileAlertsFrag).navigateToDetail()
                }

                false -> showDialogWithMessage(
                    requireContext(), concatenateText(response.errors)
                ) { d, _ -> d.dismiss() }

                else -> {}
            }
        }
    }

    private fun bindOutboxDialogItemViews(
        binding: DialogLayoutInboxOutboxBinding, item: Status
    ) {
        binding.apply {
            txtDialogTitle.text = resources.getString(R.string.ask_for_cooperation_outbox)
            btnApproveDialog.visibility = View.GONE
            btnDenyDialog.visibility = View.GONE

            item.file?.apply {
                typeId?.let { id ->
                    if (id == FileTypes().owner.id) {
                        viewPager.visibility = View.VISIBLE
                        indicator.visibility = View.VISIBLE
                        item.file.images?.apply {
                            viewPager.adapter = ImagePagerAdapter(null, null, this)
                            indicator.setViewPager(binding.viewPager)
                        }
                    } else {
                        viewPager.visibility = View.GONE
                        indicator.visibility = View.GONE
                    }
                }
                locations?.apply {
                    txtRegion.text = this[0].region.title
                }
                age?.apply {
                    txtAge.text = getPropertyPeriodsText(
                        requireContext(), this, R.string.empty, R.string.year
                    )
                }
                size?.apply {
                    txtSize.text = getPropertyPeriodsText(
                        requireContext(), this, R.string.empty, R.string.squere_meter
                    )

                    price?.let {
                        txtPrice.text = getPropertyPeriodsPriceText(requireContext(), it ,R.string.price, R.string.tooman)
                        txtPricePerMeter.text = calculatePricePerMeter(
                            requireContext(), it, this
                        )
                    }
                }
            }

            item.targetUser?.apply {
                profilePic?.apply {
                    bindImage(binding.imgUser, this)
                }
                txtAdvertiser.text = String.format("%s %s", firstName, lastName)
                txtRealEstate.text = realEstate
                txtCreateAt.text = createAt
            }
            val a = arrayOf(txtStatus1, txtStatus2, txtStatus3)
            val b = statusList.filter { it.requestId == item.requestId }
            a.forEach {
                it.visibility = View.GONE
            }
            for (i in b.indices) {
                if (i < a.size) a[i].visibility = View.VISIBLE
                getStatusStringAndColor(b[i], a[i])
            }
        }
    }

    private fun getStatusStringAndColor(item: Status, tv: TextView) {
        var stringStatus = ""
        when (item.status) {
            1 -> {
                stringStatus = resources.getString(R.string.verified)
                tv.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            }

            0 -> {
                stringStatus = resources.getString(R.string.decline)
                tv.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }

            else -> {
                stringStatus = resources.getString(R.string.to_verify)
                tv.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
            }
        }
        item.targetUser?.apply {
            if (realEstate == resources.getString(R.string.freelancer))
                tv.text = resources.getString(
                    R.string.status_text_freelancer,
                    lastName,
                    realEstate,
                    stringStatus
                )
            else
                tv.text = resources.getString(
                    R.string.status_text,
                    lastName,
                    realEstate,
                    stringStatus
                )
        }
    }

    private fun setupRecyclerView() {
        setupOutboxList()
        binding.rvInboxOutbox.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupOutboxList() {
        val adapter2 = InboxOutboxAdapter(null, this, OUTBOX)
        binding.rvInboxOutbox.adapter = adapter2
        getList()
        listenToOutboxResponse(adapter2)

    }

    fun getList() {
        (activity as MainActivity).user?.apply {
            viewModel.getOutbox(
                requireActivity(), id!!, token!!
            )
        }
    }

    private fun listenToOutboxResponse(adapter: InboxOutboxAdapter) {
        viewModel.outboxResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    response.data?.apply {
                        statusList.addAll(response.data)
                    }
                    adapter.submitList(response.data?.filter { it.isManReceiver == true })
                }

                false -> {
                    showToast(requireContext(), concatenateText(response.errors))
                    viewModel.resetOutboxResponse()
                }

                else -> Log.e("TAG", "setupOutbox: ${response.errors}")
            }
            binding.pullToRefreshMainList.isRefreshing = false
        }
    }
}
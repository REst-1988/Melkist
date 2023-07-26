package com.example.melkist.views.profile.alerts

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.STATUS_APPROVED
import com.example.melkist.utils.STATUS_DENY
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileInboxFrag(
    private val fragment: Fragment
) : Fragment() {

    private lateinit var binding: FragProfileInBoxOutBoxBinding
    private val viewModel: MainViewModel by activityViewModels()
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
        listenToAlertStatusResponse()
    }

    private fun initListeners() {
        binding.pullToRefreshMainList.setOnRefreshListener {
            setupInboxList()
        }
    }

    fun showInboxDialog(item: Status) {
        val binding = DialogLayoutInboxOutboxBinding.inflate(LayoutInflater.from(context))
        bindInboxDialogItemViews(binding, item)
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        binding.btnCloseDialog.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.btnMoreDetailDialog.setOnClickListener {
            (activity as MainActivity).user?.apply {
                try {
                    viewModel.getFileInfoById(token = token!!, item.file!!.id!!, id!!)
                    listenToFileDetailData(alertDialog)
                } catch (e: Exception) {
                    handleSystemException(e)
                }
            }
        }
        binding.btnApproveDialog.setOnClickListener {
            (activity as MainActivity).user?.apply {
                item.requestId?.let { requestId ->
                    viewModel.setAlertStatus(token!!, requestId, id!!, STATUS_APPROVED)
                    alertDialog.dismiss()
                }
            }
        }
        binding.btnDenyDialog.setOnClickListener {
            (activity as MainActivity).user?.apply {
                item.requestId?.let { requestId ->
                    viewModel.setAlertStatus(token!!, requestId, id!!, STATUS_DENY)
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun bindInboxDialogItemViews(binding: DialogLayoutInboxOutboxBinding, item: Status) {
        binding.apply {
            btnApproveDialog.visibility = View.VISIBLE
            btnDenyDialog.visibility = View.VISIBLE
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
                        txtPrice.text = getPropertyPeriodsPriceText(requireContext(), it)
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
            txtStatus1.visibility = View.GONE
            txtStatus2.visibility = View.GONE
            txtStatus3.visibility = View.GONE
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

    private fun listenToAlertStatusResponse() {
        viewModel.setStatusResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    setupInboxList()
                    viewModel.resetStatusResponse()
                }

                false -> {
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
                    viewModel.resetStatusResponse()
                }

                else -> Log.e(
                    "TAG",
                    "listenToAlertStatusResponse: ${resources.getString(R.string.null_value)}",
                )

            }
        }
    }

    private fun setupRecyclerView() {
        setupInboxList()
        binding.rvInboxOutbox.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupInboxList() {
        val adapter = InboxOutboxAdapter(this, null, INBOX)
        binding.rvInboxOutbox.adapter = adapter
        getList()
        viewModel.inboxResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    adapter
                        .submitList(
                            response.data
                        )
                }

                false -> {
                    showToast(requireContext(), concatenateText(response.errors))
                    viewModel.resetInboxResponse()
                }

                else -> {
                    Log.e("TAG", "setupOutbox: ${response.errors}")
                }
            }
            binding.pullToRefreshMainList.isRefreshing = false
        }
    }

    fun getList() {
        (activity as MainActivity).user?.apply {
            viewModel.getInbox(
                id!!, token!!
            )
        }
    }
}
package com.example.melkist.views.profile.cooperations

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.adapters.SendReceivedCooperationAdapter
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.DialogLayoutInboxOutboxBinding
import com.example.melkist.databinding.FragProfileSendRecievedCooperationBinding
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Status
import com.example.melkist.utils.SENT
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileSendCooperationFrag(
    private val fragment: ProfileCooperationFrag
) : Fragment() {

    private lateinit var binding: FragProfileSendRecievedCooperationBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileSendRecievedCooperationBinding.inflate(inflater)
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
            setupSendCooperation()
        }
    }

    private fun setupRecyclerView() {
        setupSendCooperation()
        binding.rvSendReceived.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupSendCooperation() {
        val adapter2 = SendReceivedCooperationAdapter(fragReceived = null, fragSend = this, SENT)
        binding.rvSendReceived.adapter = adapter2
        (activity as MainActivity).user?.apply {
            viewModel.getSendCooperation(
                requireActivity(),
                id!!,
                token!!
            )
        }
        viewModel.cooperationResponseListSend.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> adapter2.submitList(response.data?.filter { it.isManReceiver == true })
                false -> showToast(requireContext(), concatenateText(response.errors))
                else -> Log.e(
                    "TAG",
                    "setupSendCooperation: ${resources.getString(R.string.null_value)}"
                )

            }
            binding.pullToRefreshMainList.isRefreshing = false
        }
    }

    fun showSendDialog(item: Status) {
        val binding =
            DialogLayoutInboxOutboxBinding.inflate(LayoutInflater.from(context))
        bindReceivedDialogItemViews(binding, item)
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        initDialogClickListeners(alertDialog, binding, item)
    }

    private fun bindReceivedDialogItemViews(binding: DialogLayoutInboxOutboxBinding, item: Status) {
        binding.apply {
            txtDialogTitle.text = resources.getString(R.string.cooperation_send)
            btnCallRequest.visibility = View.VISIBLE
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
        }
    }

    private fun initDialogClickListeners(alertDialog: AlertDialog, binding: DialogLayoutInboxOutboxBinding, item: Status) {
        binding.apply {
            btnCloseDialog.setOnClickListener {
                alertDialog.dismiss()
            }
            btnMoreDetailDialog.setOnClickListener {
                (activity as MainActivity).user?.apply {
                    try {
                        viewModel.getFileInfoById(requireActivity(), token = token!!, item.file!!.id!!, id!!)
                        listenToFileDetailData(alertDialog)
                    } catch (e: Exception) {
                        handleSystemException(lifecycleScope, "$id, ProfileSendCooperationFrag, initDialogClickListeners, ", e)
                    }
                }
            }
            btnCallRequest.setOnClickListener {
                item.targetUser?.mobile?.apply {
                    val intent = Intent(Intent.ACTION_DIAL)
                    val calling = "tel:${this.mobile}"
                    intent.data = Uri.parse(calling)
                    startActivity(intent)
                }
            }
        }
    }

    private fun listenToFileDetailData(alertDialog: AlertDialog) {
        viewModel.fileAllData.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    alertDialog.dismiss()
                    (fragment as ProfileCooperationFrag).navigateToDetail()
                }

                false -> showDialogWithMessage(
                    requireContext(), concatenateText(response.errors)
                ) { d, _ -> d.dismiss() }

                else -> {}
            }
        }
    }
}
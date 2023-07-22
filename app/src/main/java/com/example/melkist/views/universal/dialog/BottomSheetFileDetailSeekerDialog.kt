package com.example.melkist.views.universal.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.LayoutBottomSheetFileDetailSeekerBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showFav
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import com.example.melkist.views.map.MapP1Frag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFileDetailSeekerDialog(
    private val fragment: MapP1Frag,
    private val fileId: Int
) : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetFileDetailSeekerBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).user?.apply {
            viewModel.getFileInfoById(token!!, fileId, id!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetFileDetailSeekerBinding.inflate(
            inflater
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initListeners(response: FileDataResponse) {
        binding.imgLogo.setOnClickListener {
            fragment.onMoreDetailFileClick(this)
        }
        binding.txtImgLogo.setOnClickListener {
            fragment.onMoreDetailFileClick(this)
        }
        binding.ibtnBookmark.setOnClickListener {
            (activity as MainActivity).user?.apply {
                viewModel.saveFavFile(token!!, id!!, fileId)
            }
            binding.ibtnBookmark.setOnClickListener {
                response.data?.isFav?.apply {
                    if (this) {
                        (activity as MainActivity).user?.apply {
                            viewModel.deleteFavFile(token!!, id!!, fileId)
                        }
                    }else {
                        (activity as MainActivity).user?.apply {
                            viewModel.saveFavFile(token!!, id!!, fileId)
                        }
                    }
                }
            }
        }
        binding.ibtnShare.setOnClickListener {
            showToast(requireContext(), resources.getString(R.string.next_phase))
        }
        binding.ibtnMore.setOnClickListener {
            fragment.onMoreDetailFileClick(this)
        }
    }

    private fun initObservers() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING -> binding.progress.visibility = View.VISIBLE
                else -> binding.progress.visibility = View.GONE
            }
        }
        viewModel.fileAllData.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> onOkGettingFileAllDataResponse(response)
                false -> showDialogWithMessage(
                    requireContext(), concatenateText(response.errors)
                ) { d, _ -> d.dismiss() }

                else -> Log.e("TAG", "fileAllData: ${resources.getString(R.string.null_value)}")
            }
        }

        viewModel.saveFavResponse.observe(viewLifecycleOwner) { response ->
            Log.e("TAG", "saveFavResponse: test $response")
            when (response.result) {
                true -> {
                    response.message?.let {
                        showToast(
                            requireContext(), it
                        )
                        binding.ibtnBookmark.setImageResource(R.drawable.baseline_bookmark_added_24)
                        viewModel.resetSaveResponse()
                        viewModel.fileAllData.value?.data?.isFav = true
                    }
                }

                false -> {
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
                    viewModel.resetSaveResponse()
                }

                else -> Log.e("TAG", "saveFavResponse: ${resources.getString(R.string.null_value)}")
            }
        }

        viewModel.deleteFavResponse.observe(viewLifecycleOwner) { response ->
            Log.e("TAG", "deleteFavResponse: test $response")
            when (response.result) {
                true -> {
                    showToast(
                        requireContext(), response.message!!
                    )
                    binding.ibtnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    viewModel.resetDeleteResponse()
                    viewModel.fileAllData.value?.data?.isFav = false
                }

                false -> {
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
                    viewModel.resetDeleteResponse()
                }

                else -> Log.e(
                    "TAG",
                    "deleteFavResponse: ${resources.getString(R.string.null_value)}"
                )
            }
        }
    }

    private fun onOkGettingFileAllDataResponse(response: FileDataResponse) {
        response.data?.apply {
            user.profilePic?.apply {
                bindImage(binding.imgUser, this)
            }
            binding.txtRoomNo.text =
                getPropertyPeriodsText(requireContext(), roomNo, R.string.room, R.string.room)
            binding.txtSize.text = getPropertyPeriodsText(
                requireContext(),
                size,
                R.string.meterage,
                R.string.squere_meter
            )
            binding.txtRegion.text = locations[0].region.title
            price.from?.apply {
                binding.txtPrice.text = getPropertyPeriodsPriceText(requireContext(), price)
            }
            Log.e("TAG", "onOkGettingFileAllDataResponse: $isFav", )
            isFav?.apply {
                binding.ibtnBookmark.showFav(this)
            }
        }
        initListeners(response)
    }
}
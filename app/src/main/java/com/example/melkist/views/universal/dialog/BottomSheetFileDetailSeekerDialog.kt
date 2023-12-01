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
import com.example.melkist.models.Period
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.isShowMortgageField
import com.example.melkist.utils.isShowRentField
import com.example.melkist.utils.isShowRoomsField
import com.example.melkist.utils.isShowSizeField
import com.example.melkist.utils.isShowTotalPriceField
import com.example.melkist.utils.onRequestFalseResult
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
        (activity as MainActivity).user.apply {
            viewModel.getFileInfoById(requireActivity(), this?.token, fileId, this?.id)
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
            fragment.onMoreDetailFileClick()
        }
        binding.txtImgLogo.setOnClickListener {
            fragment.onMoreDetailFileClick()
        }
        binding.ibtnBookmark.setOnClickListener {
            response.data?.isFav?.apply {
                if (this) {
                    (activity as MainActivity).user.apply {
                        viewModel.deleteFavFile(requireActivity(), this?.token, this?.id, fileId)
                    }
                } else {
                    (activity as MainActivity).user.apply {
                        viewModel.saveFavFile(requireActivity(), this?.token, this?.id, fileId)
                    }
                }
            }
        }
        binding.ibtnShare.setOnClickListener {
            showToast(requireContext(), resources.getString(R.string.next_phase))
        }
        binding.ibtnMore.setOnClickListener {
            fragment.onMoreDetailFileClick()
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
                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                    this.dismiss()
                }

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
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
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
                    viewModel.resetDeleteFavResponse()
                    viewModel.fileAllData.value?.data?.isFav = false
                }

                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                    viewModel.resetDeleteFavResponse()
                }

                else -> Log.e(
                    "TAG",
                    "deleteFavResponse: ${resources.getString(R.string.null_value)}"
                )
            }
        }
    }

    private fun onOkGettingFileAllDataResponse(response: FileDataResponse) {
        response.data?.let { data ->
            binding.apply {
                data.user?.profilePic?.apply {
                    bindImage(imgUser, this)
                }

                txtType.text = data.typeInfo?.fileType?.title
                txtSubCat.text = data.typeInfo?.subCategory?.title
                txtCat.text = data.typeInfo?.category?.title

                if (isShowRoomsField(
                        data.typeInfo?.category?.id ?: 0,
                        data.typeInfo?.subCategory?.id ?: 0,
                        data.typeInfo?.fileType?.id
                    )
                ) {
                    txtRoomNo.text = getPropertyPeriodsText(
                        requireContext(),
                        data.roomNo ?: Period(null, null),
                        R.string.empty,
                        R.string.room
                    )
                } else {
                    txtRoomNo.text = ""
                    txtRoomNo.visibility = View.GONE
                }

                if (isShowSizeField(
                        data.typeInfo?.category?.id ?: 0,
                        data.typeInfo?.subCategory?.id ?: 0,
                        data.typeInfo?.fileType?.id
                    )
                ) {
                    txtSize.text = getPropertyPeriodsText(
                        requireContext(),
                        data.size ?: Period(null, null),
                        R.string.meterage,
                        R.string.squere_meter
                    )
                } else {
                    txtSize.text = ""
                    txtSize.visibility = View.GONE
                }
                txtRegion.text = data.locations[0].region.title

                if (isShowTotalPriceField(
                        data.typeInfo?.category?.id ?: 0,
                        data.typeInfo?.subCategory?.id ?: 0,
                        data.typeInfo?.fileType?.id
                    )
                ) {
                    txtPrice.text = getPropertyPeriodsPriceText(
                        requireContext(),
                        data.price ?: Period(null, null),
                        R.string.price,
                        R.string.tooman
                    )
                } else {
                    txtPrice.text = ""
                    txtPrice.visibility = View.GONE
                }

                if (isShowMortgageField(
                        data.typeInfo?.category?.id ?: 0,
                        data.typeInfo?.subCategory?.id ?: 0,
                        data.typeInfo?.fileType?.id
                    )
                ) {
                    txtMortgage.text = getPropertyPeriodsPriceText(
                        requireContext(),
                        data.mortgage ?: Period(null, null),
                        R.string.mortgage_amount,
                        R.string.tooman
                    )
                } else {
                    txtMortgage.text = ""
                    txtMortgage.visibility = View.GONE
                }

                if (isShowRentField(
                        data.typeInfo?.category?.id ?: 0,
                        data.typeInfo?.subCategory?.id ?: 0,
                        data.typeInfo?.fileType?.id
                    )
                ) {
                    txtRent.text = getPropertyPeriodsPriceText(
                        requireContext(),
                        data.price ?: Period(null, null),
                        R.string.rent_amount,
                        R.string.tooman
                    )
                } else {
                    txtRent.text = ""
                    txtRent.visibility = View.GONE
                }

                data.isFav?.apply {
                    ibtnBookmark.showFav(this)
                }
            }
        }
        initListeners(response)
    }
}
package com.example.melkist.views.universal.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.LayoutBottomSheetFileDetailOwnerBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showFav
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import com.example.melkist.views.map.MapP1Frag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFileDetailOwnerDialog(
    private val fragment: MapP1Frag,
    private val fileId: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetFileDetailOwnerBinding
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
        binding = LayoutBottomSheetFileDetailOwnerBinding.inflate(
            inflater
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initListeners(response: FileDataResponse) {
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

                else -> {}
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
                    this.dismiss()
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
        response.data?.images?.apply {
            binding.viewPager.adapter =
                ImagePagerAdapter(fragment, this@BottomSheetFileDetailOwnerDialog, this)
            binding.indicator.setViewPager(binding.viewPager)
        }
        response.data?.apply {
            user?.profilePic?.apply {
                bindImage(binding.imgUser, this)
            }
            binding.txtType.text = typeInfo?.fileType?.title
            binding.txtSubCat.text = typeInfo?.subCategory?.title
            binding.txtCat.text = typeInfo?.category?.title
            roomNo?.from?.apply {
                binding.txtRoomNo.text = String.format(
                    "%s %s", this, resources.getString(R.string.room)
                )
            }
            binding.txtSize.text = size?.from?.let { size ->
                String.format(
                    "%s %s",
                    size,
                    resources.getString(R.string.squere_meter)
                )
            }?:""
            binding.txtRegion.text = locations[0].region.title
            binding.apply {
                txtPrice.visibility = View.GONE
                txtMortgage.visibility = View.GONE
                txtRent.visibility = View.GONE
            }
            binding.txtPrice.text = price?.from?.let { priceFrom ->
                binding.txtPrice.visibility = View.VISIBLE
                String.format(
                    "%s %s",
                    formatNumber(priceFrom),
                    resources.getString(R.string.tooman)
                )
            } ?: ""

            binding.txtMortgage.text = mortgage?.from?.let { mortgageFrom ->
                binding.txtMortgage.visibility = View.VISIBLE
                resources.getString(
                    R.string.mortgage_amount_place_holder,
                    formatNumber(mortgageFrom)
                )
            } ?: ""

            binding.txtRent.text = rent?.from?.let { rentFrom ->
                binding.txtRent.visibility = View.VISIBLE
                resources.getString(
                    R.string.rent_amount_place_holder,
                    formatNumber(rentFrom)
                )
            } ?: ""

            isFav?.apply {
                binding.ibtnBookmark.showFav(isFav!!)
            }
        }
        initListeners(response)
    }
}
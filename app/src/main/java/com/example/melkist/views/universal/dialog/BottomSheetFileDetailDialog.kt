package com.example.melkist.views.universal.dialog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.LayoutBottomSheetFileDetailBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import com.example.melkist.views.map.MapP1Frag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFileDetailDialog(
    private val fragment: MapP1Frag,
    private val fileId: Int
) : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetFileDetailBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).user?.apply {
            viewModel.getFileInfoById(token!!, fileId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetFileDetailBinding.inflate(
            inflater
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.ibtnBookmark.setOnClickListener {
            (activity as MainActivity).user?.apply {
                viewModel.saveFavFile(token!!, id!!, fileId)
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
                false -> showDialogWithMessage(
                    requireContext(), concatenateText(response.errors)
                ) { d, _ -> d.dismiss() }

                else -> Log.e("TAG", "initObservers: ${resources.getString(R.string.global_error)}")
            }
        }
        viewModel.saveFavResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    showToast(
                        requireContext(), resources.getString(R.string.save_accepted)
                    )
                    this@BottomSheetFileDetailDialog.dismiss()
                }

                false -> showToast(
                    requireContext(), resources.getString(R.string.global_error)
                )

                else -> showToast(
                    requireContext(), resources.getString(R.string.global_error)
                )
            }
        }
    }

    private fun onOkGettingFileAllDataResponse(response: FileDataResponse) {
        binding.viewPager.adapter = ImagePagerAdapter(fragment, response)
        binding.indicator.setViewPager(binding.viewPager)

        response.data?.apply {
            this.user.profilePic?.apply {
                val imgUri: Uri? = this.toUri().buildUpon().scheme("https").build()
                binding.imgUser.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_baseline_person_outline_24)
                }
            }
            binding.txtRoomNo.text = String.format(
                "%s %s", roomNo.from ?: "", resources.getString(R.string.room)
            )
            binding.txtSize.text = String.format(
                "%s %s",
                size.from ?: "",
                resources.getString(R.string.squere_meter)
            )
            binding.txtRegion.text = locations[0].region.title
            price.from?.apply {
                binding.txtPrice.text = String.format(
                    "%s %s",
                    formatNumber(toDouble()),
                    resources.getString(R.string.tooman)
                )
            }
        }
    }
}
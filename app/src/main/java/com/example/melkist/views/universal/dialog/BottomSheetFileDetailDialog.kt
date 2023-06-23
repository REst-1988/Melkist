package com.example.melkist.views.universal.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.LayoutBottomSheetFileDetailBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.DATA
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.dialogviewmodel.BottomSheetFileDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFileDetailDialog(
    private val fileId: Int
) : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetFileDetailBinding
    private val viewModel: BottomSheetFileDialogViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = (activity as MainActivity).user
        viewModel.getFileInfoById(user.token!!, fileId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetFileDetailBinding.inflate(
            inflater
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
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
    }

    private fun onOkGettingFileAllDataResponse(response: FileDataResponse) {
        //TODO: set The view pager here
        response.data?.apply {
            binding.txtRoomNo.text = String.format(
                "%s %s",
                response.data.roomNo.from.toString(),
                resources.getString(R.string.room)
            )
            binding.txtSize.text = String.format(
                "%s %s",
                response.data.size.from.toString(),
                resources.getString(R.string.size_squere_meter)
            )
            binding.txtRegion.text = response.data.locations[0].region.title
            binding.txtPrice.text = String.format(
                "%s %s",
                formatNumber(response.data.price.from!!.toDouble()),
                resources.getString(R.string.tooman)
            )
        }
    }

    fun onClick() {
        //TODO: connect this when user click on anywhere on dialog
        val bundle = bundleOf(DATA to viewModel.fileAllData.value)
        findNavController().navigate(R.id.action_navigation_map_to_fileDetailFrag, bundle)
    }
}
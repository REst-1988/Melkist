package com.example.melkist.views.universal.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.LayoutBottomSheetFileDetailBinding
import com.example.melkist.models.FileAllDataResponse
import com.example.melkist.models.Location
import com.example.melkist.models.Period
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFileDetailDialog: BottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetFileDetailBinding
    private val viewModel: MapViewModel by activityViewModels ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = LayoutBottomSheetFileDetailBinding.inflate(
            inflater
        )
        Log.e("TAG", "onCreateView: $binding", )
/*      val behavior = BottomSheetBehavior.from(binding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.peekHeight = 200
        behavior.isHideable = true*/
/*        binding.expandButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.collapseButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.status.observe(viewLifecycleOwner){
            when(it){
                ApiStatus.LOADING -> binding.progress.visibility = View.VISIBLE
                else -> binding.progress.visibility = View.GONE
            }
        }
        viewModel.fileAllData.observe(viewLifecycleOwner){ response ->
            when (response.result) {
                true -> onOkGettingFileAllDataResponse(response)
                false ->
                    showDialogWithMessage(
                        requireContext(),
                        concatenateText(response.errors)
                    ) { d, _ ->
                        d.dismiss()
                    }
                else ->
                    Log.e("TAG", "initObservers: ${resources.getString(R.string.global_error)}", )
            }
        }
    }

    private fun onOkGettingFileAllDataResponse(response: FileAllDataResponse) {
        binding.txtAdvertiser.text = String.format("%s %s", response.data!!.user.firstName, response.data.user.lastName)
        // TODO: binding.txtRealEstate.text = TODO("getting user real estate data from Amir")
        binding.txtRegion.text = response.data.locations[0].region.title
        showOtherRegions(response.data.locations)
        // TODO: Get age from server
        binding.txtSize.text = getPropertyPeriodsText(response.data.size)
        binding.txtRoomNo.text = getPropertyPeriodsText(response.data.roomNo)
        binding.txtPrice.text = getPropertyPeriodsText(response.data.price)
        binding.txtPricePerMeter.text = calculatePricePerMeter (response.data.price, response.data.size)
        binding.txtDescriptions.text = response.data.description

    }

    private fun showOtherRegions(locations: List<Location>){
        var text = ""
        if (locations.size > 1){
            binding.txtRegionOther.visibility = View.VISIBLE
            for (i in 1 until locations.size)
                text +=locations[i].region.title
        }else {
            binding.txtRegionOther.visibility = View.GONE
        }
        binding.txtRegionOther.text = text
    }

    private fun getPropertyPeriodsText (period: Period): String{
        val conjunctions = if (period.to == period.from)
            ""
        else
            resources.getString(R.string.to) + " " + period.to
        return String.format("%s %s", period.from, conjunctions)
    }

    private fun calculatePricePerMeter (price: Period, size: Period): String {
        return if (price.to == price.from)
            (price.from/size.from).toString()
        else
            (price.from/size.from).toString() + " " + resources.getString(R.string.to) + " " + price.to/size.to
    }

    /*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the parent fragment's view
        val parentFragmentView = parentFragment?.view

        // Access the bottom sheet behavior
        val bottomSheetBehavior = BottomSheetBehavior.from(parentFragmentView?.parent as View)

        // Set the peek height
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.rounded_btns)

        // Set the initial state
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Enable collapsing the dialog when dragged to the peek height
        bottomSheetBehavior.isHideable = true

    }*/

/*    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.Theme_Melkist)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return dialog
    }*/
}
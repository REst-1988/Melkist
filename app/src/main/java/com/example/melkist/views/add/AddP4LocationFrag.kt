package com.example.melkist.views.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP4LocationBinding
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.AddItemViewModel

class AddP4LocationFrag : Fragment() {

    private lateinit var binding: FragAddP4LocationBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP4LocationBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP4LocationFrag
        }
        return binding.root
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP1MainFrag
        )
    }

    fun cancel() {
        // TODO
    }

    fun mapVisibility(): Boolean {
        if (viewModel.getItemType() == AddItemViewModel.ItemType.SEEKER) {
            viewModel.lat = null
            viewModel.lng = null
            viewModel.isShowExactAddress = false
            return false
        }
        return true
    }

    fun onChoosingProvince() {
        viewModel.setLocReqSource(AddItemViewModel.Cr.PROVINCE)
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP5CrFrag
        )
    }

    fun onChoosingCity() {
        viewModel.setLocReqSource(AddItemViewModel.Cr.CITY)
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP5CrFrag
        )
    }

    fun onChoosingRegion1() {
        if (viewModel.isReadyForChooseRegion()) {
            viewModel.setLocReqSource(AddItemViewModel.Cr.REGION_1)
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.choose_province_city_alert_message)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        }
    }

    fun onChoosingLocation() {
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP5MapsFrag
        )
    }

    fun isShowIsShowExactAddress(): Boolean {
        return viewModel.getItemType() == AddItemViewModel.ItemType.OWNER
    }

    fun onCheckedChanged(check: Boolean){
        viewModel.isShowExactAddress = check
    }

    fun isShowOtherLocations(): Boolean {
        return viewModel.getItemType() == AddItemViewModel.ItemType.SEEKER
    }

    fun onChoosingRegion2() {
        if (viewModel.isReadyForChooseRegion()) {
            viewModel.setLocReqSource(AddItemViewModel.Cr.REGION_2)
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.choose_province_city_alert_message)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        }
    }

    fun onChoosingRegion3() {
        if (viewModel.isReadyForChooseRegion()) {
            viewModel.setLocReqSource(AddItemViewModel.Cr.REGION_3)
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.choose_province_city_alert_message)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        }
    }

    fun onProceed() {
        Log.e("TAG", "onProceed: check ${viewModel.isShowExactAddress}", )
        when (viewModel.getItemType()) {
            AddItemViewModel.ItemType.SEEKER ->
                findNavController().navigate(R.id.action_addP4LocationFrag_to_addP6DetailsSeekerFrag)
            AddItemViewModel.ItemType.OWNER ->
                findNavController().navigate(R.id.action_addP4LocationFrag_to_addP7DetailsOwnerFrag)
            else -> {}
        }
    }

    fun isShowProceedBtn(): Boolean {
        return viewModel.provinceId != 0 && viewModel.cityId != 0 && viewModel.lat != null && viewModel.lng != null
    }

    // TODO: this is only for preview and should be replaced by what we get from map after finishing map part or delete
    fun isShowMapImage(): Boolean = viewModel.lat != null && viewModel.lng != null

}
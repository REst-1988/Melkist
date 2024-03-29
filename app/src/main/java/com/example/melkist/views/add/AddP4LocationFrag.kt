package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP4LocationBinding
import com.example.melkist.utils.CR_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.handleSystemException
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            readyProvinceAndCity()
            viewModel.mapSnapShot.observe(viewLifecycleOwner) {
                binding.imgMapSnapshot.setImageBitmap(it)
            }
            setFragmentResultListener(CR_KEY) { _, bundle ->
                bundle.getStringArray(DATA)?.apply {
                    when (viewModel.getLocReqSource()) {
                        AddItemViewModel.Cr.PROVINCE -> {
                            viewModel.provinceId = this[0].toInt()
                            viewModel.provinceTitle = this[1]
                            viewModel.resetAddLocationFieldsByProvince()
                        }

                        AddItemViewModel.Cr.CITY -> {
                            viewModel.cityId = this[0].toInt()
                            viewModel.cityTitle = this[1]
                            viewModel.resetAddLocationFieldsByCity()
                        }

                        AddItemViewModel.Cr.REGION_1 -> {
                            viewModel.regionId = this[0].toInt()
                            viewModel.regionTitle = this[1]
                            viewModel.regionLat = this[2].toDouble()
                            viewModel.regionLng = this[3].toDouble()
                            viewModel.resetAddLocationFieldsByRegion1()
                        }

                        AddItemViewModel.Cr.REGION_2 -> {
                            viewModel.region2Id = this[0].toInt()
                            viewModel.region2Title = this[1]
                        }

                        AddItemViewModel.Cr.REGION_3 -> {
                            viewModel.region3Id = this[0].toInt()
                            viewModel.region3Title = this[1]
                        }
                    }
                }
            }
        } catch (e: Exception){
            handleSystemException(lifecycleScope, "${(activity as AddActivity).user?.id}, AddP4LocationFrag, onViewCreated,", e)
        }
    }

    private fun readyProvinceAndCity() {
        if (viewModel.provinceId == 0)
            viewModel.provinceId = (activity as AddActivity).user?.provinceId ?: 17
        if (viewModel.cityId == 0)
            viewModel.cityId = (activity as AddActivity).user?.cityId ?: 733
        if (viewModel.provinceTitle == "")
            viewModel.provinceTitle =
                (activity as AddActivity).user?.provinceTitle ?: resources.getString(R.string.fars)
        if (viewModel.cityTitle == "")
            viewModel.cityTitle =
                (activity as AddActivity).user?.cityTitle ?: resources.getString(R.string.shiraz)
    }

    private fun isReadyForNextFrag(): Boolean {
        return viewModel.provinceId != 0 &&
                viewModel.cityId != 0 &&
                viewModel.regionId != 0 &&
                viewModel.lat != null &&
                viewModel.lng != null
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().popBackStack()
        viewModel.resetAddLocationFieldsByCity()
    }

    fun cancel() {
        requireActivity().finish()
    }

    /*    fun mapVisibility(): Boolean {
            if (viewModel.getItemType() == AddItemViewModel.ItemType.SEEKER) {
                viewModel.lat = null
                viewModel.lng = null
                viewModel.isShowExactAddress = false
                return false
            }
            return true
        }*/

    fun onChoosingProvince() {
        viewModel.setLocReqSource(AddItemViewModel.Cr.PROVINCE)
        val bundle = bundleOf(
            CR_KEY to arrayListOf(
                viewModel.getLocReqSourceKey(), 0
            )
        )
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP5CrFrag,
            bundle
        )
    }

    fun onChoosingCity() {
        viewModel.setLocReqSource(AddItemViewModel.Cr.CITY)
        val bundle = bundleOf(
            CR_KEY to arrayListOf(
                viewModel.getLocReqSourceKey(),
                viewModel.provinceId // Note that in the future city id might change (could be variable)
            )
        )
        findNavController().navigate(
            R.id.action_addP4LocationFrag_to_addP5CrFrag,
            bundle
        )
    }

    fun onChoosingRegion1() {
        if (viewModel.isReadyForChooseRegion()) {
            viewModel.setLocReqSource(AddItemViewModel.Cr.REGION_1)
            val bundle = bundleOf(
                CR_KEY to arrayListOf(
                    viewModel.getLocReqSourceKey(), viewModel.cityId
                )
            )
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag,
                bundle
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.choose_province_city_alert_message)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        }
    }

    fun onChoosingLocation() {
        if (viewModel.regionId != 0)
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5MapsFrag
            )
        else
            showToast(requireContext(), resources.getString(R.string.choose_region_first))
    }

    /*    fun isShowIsShowExactAddress(): Boolean {
            return viewModel.getItemType() == AddItemViewModel.ItemType.OWNER
        }

        fun onCheckedChanged(check: Boolean) {
            viewModel.isShowExactAddress = check
        }*/

    fun isShowOtherLocations(): Boolean {
        return viewModel.getItemType() == AddItemViewModel.ItemType.SEEKER
    }

    fun onChoosingRegion2() {
        if (viewModel.isReadyForChooseRegion()) {
            viewModel.setLocReqSource(AddItemViewModel.Cr.REGION_2)
            val bundle = bundleOf(
                CR_KEY to arrayListOf(
                    viewModel.getLocReqSourceKey(), viewModel.cityId
                )
            )
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag,
                bundle
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
            val bundle = bundleOf(
                CR_KEY to arrayListOf(
                    viewModel.getLocReqSourceKey(), viewModel.cityId
                )
            )
            findNavController().navigate(
                R.id.action_addP4LocationFrag_to_addP5CrFrag,
                bundle
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.choose_province_city_alert_message)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
        }
    }

    fun onProceed() {
        if (isReadyForNextFrag()) {
            when (viewModel.getItemType()) {
                AddItemViewModel.ItemType.SEEKER ->
                    findNavController().navigate(R.id.action_addP4LocationFrag_to_addP6DetailsSeekerFrag)

                AddItemViewModel.ItemType.OWNER ->
                    findNavController().navigate(R.id.action_addP4LocationFrag_to_addP7DetailsOwnerFrag)

                else -> {}
            }
        } else
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.complete_all_fields)
            ) { d, _ ->
                d.dismiss()
            }
    }

    fun isShowProceedBtn(): Boolean {
        return viewModel.provinceId != 0 &&
                viewModel.cityId != 0 &&
                viewModel.regionId != 0 &&
                viewModel.lat != null &&
                viewModel.lng != null
    }

    fun isShowMapImage(): Boolean {
        return viewModel.lat != null && viewModel.lng != null
    }
}
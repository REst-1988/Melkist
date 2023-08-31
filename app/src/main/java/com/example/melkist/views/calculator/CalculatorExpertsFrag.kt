package com.example.melkist.views.calculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentCalculatorExpertsBinding
import com.example.melkist.utils.ALLAY_TAG
import com.example.melkist.utils.DATA
import com.example.melkist.utils.DIRECTION_TAG
import com.example.melkist.utils.FLOOR_TAG
import com.example.melkist.utils.LIGHTS_TAG
import com.example.melkist.utils.ROOM_TAG
import com.example.melkist.utils.UNITS_IN_FLOOR
import com.example.melkist.utils.UNITS_TAG
import com.example.melkist.utils.addLiveSeparatorListenerWithNumToLetterCallback
import com.example.melkist.utils.getRemovedSeparatorValue
import com.example.melkist.utils.showAgeDialog
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showInputDialog
import com.example.melkist.viewmodels.CalculatorExpertViewModel
import com.example.melkist.views.calculator.dialog.CalculatorResultExpertDialogFrag
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList

class CalculatorExpertsFrag : Fragment() {

    private lateinit var binding: FragmentCalculatorExpertsBinding
    private val viewModel: CalculatorExpertViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorExpertsBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@CalculatorExpertsFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etMaxPriceChild.addLiveSeparatorListenerWithNumToLetterCallback(
            binding.txtMaxPriceToLetter, resources.getString(R.string.tooman)
        )
        binding.etMinPriceChild.addLiveSeparatorListenerWithNumToLetterCallback(
            binding.txtMinPriceToLetter, resources.getString(R.string.tooman)
        )
    }

    override fun onResume() {
        super.onResume()
/*        viewModel.apply {
            buildYear = 1395
            size = 137
            room = resources.getStringArray(R.array.room_no_list)[3]
            roomNo = 2
            floor = resources.getStringArray(R.array.floor_list)[4]
            floorNo = 4
            units = resources.getStringArray(R.array.units_list)[4]
            isMoreThan18units = true
            allay = resources.getStringArray(R.array.allay_list)[1]
            isAllayWidthMoreThan4 = true
            direction = resources.getStringArray(R.array.direction_list)[0]
            isNorthern = true
        }
        binding.apply {
            etMinPriceChild.setText("80000000")
            etMaxPriceChild.setText("90000000")
            switchUnlocked.isChecked = true
            switchElevator.isChecked = true
            switchParking.isChecked = true
            switchCommercial.isChecked = false

            txtChooseAge.text = showAgeText()
            txtChooseSize.text = showSizeText()
            txtChooseRoom.text = showRoomText()
            txtChooseFloor.text = showFloorText()
            txtChooseUnits.text = showUnitsText()
            txtChooseAllay.text = showAllayText()
            txtChooseNorthern.text = showNorthernText()
        }*/
    }

    /******************* binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onBtnCalculateClick() {
        viewModel.showItemLogs()
        viewModel.minNewPropertyValue = binding.etMinPriceChild.getRemovedSeparatorValue()
        viewModel.maxNewPropertyValue = binding.etMaxPriceChild.getRemovedSeparatorValue()
        // : postal code should sent to server
        if (viewModel.isAllFieldsOkay()) {
            viewModel.calculatePrice()
            CalculatorResultExpertDialogFrag(
                R.style.dialog_theme,
                viewModel.housePrice,
                viewModel.housePricePerMeter,
                viewModel.rentPrice
            ).show(
                childFragmentManager,
                "expert_calculator"
            )
        } else {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.expert_calculator_dialog_fill_fields)
            ) { d, _ ->
                d.dismiss()
            }
        }
    }

    fun onUnlockSwitchSelect(check: Boolean) {
        viewModel.isUnlock = check
        if (check) {
            binding.txtUnlockedYes.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtUnlockedNo.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        } else {
            binding.txtUnlockedNo.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtUnlockedYes.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        }
    }

    fun onTxtUnlockYesClick() {
        binding.switchUnlocked.isChecked = true
        onUnlockSwitchSelect(true)
    }

    fun onTxtUnlockNoClick() {
        binding.switchUnlocked.isChecked = false
        onUnlockSwitchSelect(false)
    }

    fun onTxtElevatorYesClick() {
        binding.switchElevator.isChecked = true
        onElevatorSwitchSelect(true)
    }

    fun onTxtElevatorNoClick() {
        binding.switchElevator.isChecked = false
        onElevatorSwitchSelect(false)
    }

    fun onTxtParkingYesClick() {
        binding.switchParking.isChecked = true
        onParkingSwitchSelect(true)
    }

    fun onTxtParkingNoClick() {
        binding.switchParking.isChecked = false
        onParkingSwitchSelect(false)
    }

    fun onTxtCommercialYesClick() {
        binding.switchCommercial.isChecked = true
        onCommercialSwitchSelect(true)
    }

    fun onTxtCommercialNoClick() {
        binding.switchCommercial.isChecked = false
        onCommercialSwitchSelect(false)
    }

    fun onElevatorSwitchSelect(check: Boolean) {
        viewModel.isHasElevator = check
        if (check) {
            binding.txtElevatorYes.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtElevatorNo.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        } else {
            binding.txtElevatorNo.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtElevatorYes.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        }
    }

    fun onParkingSwitchSelect(check: Boolean) {
        viewModel.isHasParking = check
        if (check) {
            binding.txtParkingYes.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtParkingNo.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        } else {
            binding.txtParkingNo.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtParkingYes.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        }
    }

    fun onCommercialSwitchSelect(check: Boolean) {
        viewModel.isHasCommercialUnit = check
        if (check) {
            binding.txtCommercialYes.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtCommercialNo.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        } else {
            binding.txtCommercialNo.setTextColor(resources.getColor(R.color.normal_text_color))
            binding.txtCommercialYes.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
        }
    }

    fun onChoosingAge() {
        showAgeDialog(
            requireContext(), viewLifecycleOwner, resources.getString(R.string.age_title)
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.buildYear = it.toInt()
                binding.txtChooseAge.text = showAgeText()
            }
        }
    }

    fun showAgeText(): String {
        return if (viewModel.buildYear == 0) {
            binding.txtChooseAge.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.enter)
        } else {
            Log.e("", "showAgeText: ${viewModel.buildYear}")
            binding.txtChooseAge.setTextColor(resources.getColor(R.color.normal_text_color))
            String.format(
                "%s %s",
                viewModel.buildYear.toString(),
                resources.getString(R.string.year)
            )
        }
    }

    fun onChoosingSize() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.measurement),
            resources.getString(R.string.meter_squere),
            6
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.size = it.toInt()
                binding.txtChooseSize.text = showSizeText()
            }
        }
    }

    fun showSizeText(): String {
        return if (viewModel.size == 0) {
            binding.txtChooseSize.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.enter)
        } else {
            binding.txtChooseSize.setTextColor(resources.getColor(R.color.normal_text_color))
            String.format(
                "%s %s", viewModel.size.toString(), resources.getString(R.string.squere_meter)
            )
        }
    }

    fun onChoosingRoom() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_TAG)
        bottomFrag.setFragmentResultListener(ROOM_TAG) { _, positionBundle ->
            viewModel.room =
                resources.getStringArray(R.array.room_no_list)[positionBundle.getInt(DATA)]
            viewModel.roomNo = positionBundle.getInt(DATA)
            binding.txtChooseRoom.text = showRoomText()
        }
    }

    fun showRoomText(): String {
        return if (viewModel.room == "") {
            binding.txtChooseRoom.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseRoom.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.room
        }
    }

    fun onChoosingFloor() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.floor_list).toList())
        bottomFrag.show(childFragmentManager, FLOOR_TAG)
        bottomFrag.setFragmentResultListener(FLOOR_TAG) { _, positionBundle ->
            viewModel.floor =
                resources.getStringArray(R.array.floor_list)[positionBundle.getInt(DATA)]
            viewModel.floorNo = positionBundle.getInt(DATA)
            binding.txtChooseFloor.text = showFloorText()
        }
    }

    fun showFloorText(): String {
        return if (viewModel.floor == "") {
            binding.txtChooseFloor.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseFloor.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.floor
        }
    }

    fun onChoosingUnitsInFloor() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.units_in_floor_list).toList())
        bottomFrag.show(childFragmentManager, UNITS_IN_FLOOR)
        bottomFrag.setFragmentResultListener(UNITS_IN_FLOOR) { _, positionBundle ->
            viewModel.unitsInFloor =
                resources.getStringArray(R.array.units_in_floor_list)[positionBundle.getInt(DATA)]
            viewModel.isSingleUnits = positionBundle.getInt(DATA) == 0
            binding.txtChooseUnitsInFloor.text = showUnitsInFloorText()
        }
    }

    fun showUnitsInFloorText(): String {
        return if (viewModel.unitsInFloor == "") {
            binding.txtChooseUnitsInFloor.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseUnitsInFloor.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.unitsInFloor
        }
    }

    fun onChoosingUnits() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.units_list).toList())
        bottomFrag.show(childFragmentManager, UNITS_TAG)
        bottomFrag.setFragmentResultListener(UNITS_TAG) { _, positionBundle ->
            viewModel.units =
                resources.getStringArray(R.array.units_list)[positionBundle.getInt(DATA)]
            viewModel.isMoreThan18units = positionBundle.getInt(DATA) >= 3
            binding.txtChooseUnits.text = showUnitsText()
        }
    }

    fun showUnitsText(): String {
        return if (viewModel.units == "") {
            binding.txtChooseUnits.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {

            binding.txtChooseUnits.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.units
        }
    }

    fun onChoosingAllay() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.allay_list).toList())
        bottomFrag.show(childFragmentManager, ALLAY_TAG)
        bottomFrag.setFragmentResultListener(ALLAY_TAG) { _, positionBundle ->
            viewModel.allay =
                resources.getStringArray(R.array.allay_list)[positionBundle.getInt(DATA)]
            viewModel.isAllayWidthMoreThan4 = positionBundle.getInt(DATA) >= 1
            binding.txtChooseAllay.text = showAllayText()
        }
    }

    fun showAllayText(): String {
        return if (viewModel.allay == "") {
            binding.txtChooseAllay.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseAllay.setTextColor(resources.getColor(R.color.normal_text_color))
            String.format("%s %s", viewModel.allay, resources.getString(R.string.meter))
        }
    }

    fun onChoosingNorthern() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.direction_list).toList())
        bottomFrag.show(childFragmentManager, DIRECTION_TAG)
        bottomFrag.setFragmentResultListener(DIRECTION_TAG) { _, positionBundle ->
            viewModel.direction =
                resources.getStringArray(R.array.direction_list)[positionBundle.getInt(DATA)]
            viewModel.isNorthern = positionBundle.getInt(DATA) == 0
            binding.txtChooseNorthern.text = showNorthernText()
        }
    }

    fun showNorthernText(): String {
        return if (viewModel.direction == "") {
            binding.txtChooseNorthern.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseNorthern.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.direction
        }
    }

    fun onChoosingLights() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.lights_list).toList())
        bottomFrag.show(childFragmentManager, LIGHTS_TAG)
        bottomFrag.setFragmentResultListener(LIGHTS_TAG) { _, positionBundle ->
            viewModel.lights =
                resources.getStringArray(R.array.lights_list)[positionBundle.getInt(DATA)]
            viewModel.isLights = positionBundle.getInt(DATA) != 0
            binding.txtChooseLights.text = showLightsText()
        }
    }

    fun showLightsText(): String {
        return if (viewModel.lights == "") {
            binding.txtChooseLights.setTextColor(resources.getColor(R.color.sub_item_text_color_fade))
            resources.getString(R.string.choose)
        } else {
            binding.txtChooseLights.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.lights
        }
    }
}
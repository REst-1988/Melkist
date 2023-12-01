package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP6DetailsSeekerBinding
import com.example.melkist.utils.AGE_FROM_TAG
import com.example.melkist.utils.AGE_TO_TAG
import com.example.melkist.utils.DATA
import com.example.melkist.utils.MORTGAGE_FROM_TAG
import com.example.melkist.utils.MORTGAGE_TO_TAG
import com.example.melkist.utils.PRICE_FROM_TAG
import com.example.melkist.utils.PRICE_TO_TAG
import com.example.melkist.utils.RENT_FROM_TAG
import com.example.melkist.utils.ROOM_FROM_TAG
import com.example.melkist.utils.ROOM_TO_TAG
import com.example.melkist.utils.SIZE_FROM_TAG
import com.example.melkist.utils.SIZE_TO_TAG
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.addLiveSeparatorListener
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPersianYear
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal

class AddP6DetailsSeekerFrag : Fragment() {
    private lateinit var binding: FragAddP6DetailsSeekerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP6DetailsSeekerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP6DetailsSeekerFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.extra?.apply {
            binding.txtTitle.text = resources.getString(R.string.edit_detail_page_title)
            // according to the meeting no need to add owner for seeker
            setExtraFieldsForEditing()
        }
        viewModel.saveEditResponse.observe(viewLifecycleOwner) {
            when (it.result) {
                true -> showDialogWithMessage(
                    requireContext(), it.message ?: ""
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    cancel()
                }

                false -> onRequestFalseResult(
                    requireActivity(),
                    it.errors ?: UNKNOWN_ERRORS_LIST
                ){}

                else -> showDialogWithMessage(
                    requireContext(), resources.getString(R.string.global_error)
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.descriptions?.apply {
            binding.etDescriptions.editText?.setText(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (binding.etDescriptions.editText?.text.toString().isNotEmpty())
            viewModel.descriptions = binding.etDescriptions.editText?.text.toString()
    }

    private fun setExtraFieldsForEditing(){
        binding.apply {
            viewModel.ageFrom?.apply { etAgeFrom.editText?.setText(this.toString()) }
            viewModel.ageTo?.apply { etAgeTo.editText?.setText(this.toString()) }
            viewModel.sizeFrom?.apply { etSizeFrom.editText?.setText(this.toString()) }
            viewModel.sizeTo?.apply { etSizeTo.editText?.setText(this.toString()) }
            viewModel.roomFrom?.apply { etRoomNoFrom.editText?.setText(this.toString()) }
            viewModel.roomTo?.apply { etRoomNoTo.editText?.setText(this.toString()) }
            viewModel.totalPriceFrom?.apply {
                etPriceFromChild.addLiveSeparatorListener()
                etPriceFrom.editText?.setText(this.toString()) }
            viewModel.totalPriceTo?.apply {
                etPriceToChild.addLiveSeparatorListener()
                etPriceTo.editText?.setText(this.toString())
            }
            viewModel.mortgageFrom?.apply {
                etMortgageFromChild.addLiveSeparatorListener()
                etMortgageFrom.editText?.setText(this.toString())
            }
            viewModel.mortgageTo?.apply {
                etMortgageToChild.addLiveSeparatorListener()
                etMortgageTo.editText?.setText(this.toString())
            }
            viewModel.rentFrom?.apply {
                etRentFromChild.addLiveSeparatorListener()
                etRentFrom.editText?.setText(this.toString())
            }
            viewModel.rentTo?.apply {
                etRentToChild.addLiveSeparatorListener()
                etRentTo.editText?.setText(this.toString())
            }
        }
    }

    private fun checkFieldsForNullabilitySeparatorAccepted(view: TextInputLayout): Long? {
        if (view.editText!!.text.toString()
                .isNotEmpty() && view.editText!!.text.toString() != "" && view.editText!!.text.toString() != "0"
        ) {
            if (view.editText!!.text.toString()
                    .contains(",")
            ) return BigDecimal(view.editText!!.text.toString().replace(",", "").replace("٬", "")).toLong()
            return view.editText!!.text.toString().toLong()
        }
        return null
    }

    private fun gatheringData() {
        binding.apply {
            viewModel.roomFrom = checkFieldsForNullabilitySeparatorAccepted(etRoomNoFrom)?.toInt()
            viewModel.roomTo = checkFieldsForNullabilitySeparatorAccepted(etRoomNoTo)?.toInt()
            viewModel.ageFrom = checkFieldsForNullabilitySeparatorAccepted(etAgeFrom)?.toInt()
            viewModel.ageTo = checkFieldsForNullabilitySeparatorAccepted(etAgeTo)?.toInt()
            viewModel.totalPriceFrom = checkFieldsForNullabilitySeparatorAccepted(etPriceFrom)
            viewModel.totalPriceTo = checkFieldsForNullabilitySeparatorAccepted(etPriceTo)
            viewModel.sizeFrom = checkFieldsForNullabilitySeparatorAccepted(etSizeFrom)?.toInt()
            viewModel.sizeTo = checkFieldsForNullabilitySeparatorAccepted(etSizeTo)?.toInt()
            viewModel.mortgageFrom = checkFieldsForNullabilitySeparatorAccepted(etMortgageFrom)
            viewModel.mortgageTo = checkFieldsForNullabilitySeparatorAccepted(etMortgageTo)
            viewModel.rentFrom = checkFieldsForNullabilitySeparatorAccepted(etRentFrom)
            viewModel.rentTo = checkFieldsForNullabilitySeparatorAccepted(etRentTo)
            viewModel.descriptions = etDescriptions.editText?.text?.toString()
        }
    }

/*    private fun addLiveSeparator(et: TextInputEditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                et.removeTextChangedListener(this)
                val text: String = p0.toString()
                if (!TextUtils.isEmpty(text)) {
                    val format: String = formatNumber(BigDecimal(text.replace(",", "").replace("٬", "")).toLong())
                    et.setText(format)
                    et.setSelection(format.length)
                }
                et.addTextChangedListener(this)
            }
        })
    }*/

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP6DetailsSeekerFrag_to_addP4LocationFrag)
    }

    fun cancel() {
        requireActivity().finish()
    }

    fun onCommit() {
        gatheringData()
        (activity as AddActivity).user.apply {
            viewModel.extra?.let {
                viewModel.editFile(requireActivity(), this)
            }?: viewModel.saveFile(requireActivity(), this)
        }

    }

    fun isShowAge(): Int {
        return if (viewModel.isShowAgeField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onAgeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_FROM_TAG)
        bottomFrag.setFragmentResultListener(AGE_FROM_TAG) { _, positionBundle ->
            val year = getPersianYear() - resources.getStringArray(
                R.array.int_age_list
            )[positionBundle.getInt(DATA)].toInt()
            binding.etAgeFromChild.setText(year.toString())
        }
    }

    fun onAgeToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_TO_TAG)
        bottomFrag.setFragmentResultListener(AGE_TO_TAG) { _, positionBundle ->
            val year = getPersianYear() - resources.getStringArray(
                R.array.int_age_list
            )[positionBundle.getInt(DATA)].toInt()
            binding.etAgeToChild.setText(year.toString())
        }
    }

    fun onSizeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.size_list).toList())
        bottomFrag.show(childFragmentManager, SIZE_FROM_TAG)
        bottomFrag.setFragmentResultListener(SIZE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainSizeFrom.visibility = View.GONE
                binding.etSizeFromChild.requestFocus()
            } else {
                binding.etSizeFromChild.setText(resources.getStringArray(R.array.int_size_list)[position])
            }
        }
    }

    fun onSizeToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.size_list).toList())
        bottomFrag.show(childFragmentManager, SIZE_TO_TAG)
        bottomFrag.setFragmentResultListener(SIZE_TO_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainSizeTo.visibility = View.GONE
                binding.etSizeToChild.requestFocus()
            } else {
                binding.etSizeToChild.setText(resources.getStringArray(R.array.int_size_list)[position])
            }
        }
    }

    fun isShowRooms(): Int {
        return if (viewModel.isShowRoomsField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onRoomNoFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_FROM_TAG)
        bottomFrag.setFragmentResultListener(ROOM_FROM_TAG) { _, positionBundle ->
            binding.etRoomNoFromChild.setText(
                resources.getStringArray(R.array.int_room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onRoomNoToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_TO_TAG)
        bottomFrag.setFragmentResultListener(ROOM_TO_TAG) { _, positionBundle ->
            binding.etRoomNoToChild.setText(
                resources.getStringArray(R.array.int_room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun isShowTotalPrice(): Int {
        return if (viewModel.isShowTotalPriceField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onPriceFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_FROM_TAG)
        bottomFrag.setFragmentResultListener(PRICE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainPriceFrom.visibility = View.GONE
                binding.etPriceFromChild.requestFocus()
                binding.etPriceFromChild.addLiveSeparatorListener()
            } else {
                binding.etPriceFromChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toLong()
                    )
                )
            }
        }
    }

    fun onPriceToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_TO_TAG)
        bottomFrag.setFragmentResultListener(PRICE_TO_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainPriceTo.visibility = View.GONE
                binding.etPriceToChild.requestFocus()
                binding.etPriceToChild.addLiveSeparatorListener()
            } else {
                binding.etPriceToChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toLong()
                    )
                )
            }
        }
    }

    fun isShowMortgage(): Int {
        return if (viewModel.isShowMortgageField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onMortgageFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list_mortgage).toList())
        bottomFrag.show(childFragmentManager, MORTGAGE_FROM_TAG)
        bottomFrag.setFragmentResultListener(MORTGAGE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainMortgageFrom.visibility = View.GONE
                binding.etMortgageFromChild.requestFocus()
                binding.etMortgageFromChild.addLiveSeparatorListener()
            } else {
                binding.etMortgageFromChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list_mortgage)[position].toLong()
                    )
                )
            }
        }
    }

    fun onMortgageToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list_mortgage).toList())
        bottomFrag.show(childFragmentManager, MORTGAGE_TO_TAG)
        bottomFrag.setFragmentResultListener(MORTGAGE_TO_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainMortgageTo.visibility = View.GONE
                binding.etMortgageToChild.requestFocus()
                binding.etMortgageToChild.addLiveSeparatorListener()
            } else {
                binding.etMortgageToChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list_mortgage)[position].toLong()
                    )
                )
            }
        }
    }

    fun isShowRent(): Int {
        return if (viewModel.isShowRentField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onRentFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list_rent).toList())
        bottomFrag.show(childFragmentManager, RENT_FROM_TAG)
        bottomFrag.setFragmentResultListener(RENT_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainRentFrom.visibility = View.GONE
                binding.etRentFromChild.requestFocus()
                binding.etRentFromChild.addLiveSeparatorListener()
            } else {
                binding.etRentFromChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list_rent)[position].toLong()
                    )
                )
            }
        }
    }

    fun onRentToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list_rent).toList())
        bottomFrag.show(childFragmentManager, RENT_FROM_TAG)
        bottomFrag.setFragmentResultListener(RENT_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainRentTo.visibility = View.GONE
                binding.etRentToChild.requestFocus()
                binding.etRentToChild.addLiveSeparatorListener()
            } else {
                binding.etRentToChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list_rent)[position].toLong()
                    )
                )
            }
        }
    }

/*    fun onTxtSuitableForAllClick() {
        // TODO
    }

    fun onSuitableForSwitchSelect() {
        // TODO
    }

    fun onTxtSuitableForFamilyClick() {
        // TODO
    }*/

    fun isShowFloor(): Int {
        return View.GONE
        //if (viewModel.isShowFloorField())
            // View.VISIBLE
        // else
            //View.GONE
    }

/*    fun onFloorFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.floor_list).toList())
        bottomFrag.show(childFragmentManager, FLOOR_FROM_TAG)
        bottomFrag.setFragmentResultListener(FLOOR_FROM_TAG) { _, positionBundle ->
            binding.etFloorFromChild.setText(
                positionBundle.getInt(DATA)
            )
        }
    }

    fun onFloorToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.floor_list).toList())
        bottomFrag.show(childFragmentManager, FLOOR_TO_TAG)
        bottomFrag.setFragmentResultListener(FLOOR_TO_TAG) { _, positionBundle ->
            binding.etFloorToChild.setText(
                positionBundle.getInt(DATA)
            )
        }
    }*/
}
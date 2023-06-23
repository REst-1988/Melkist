package com.example.melkist.views.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import com.example.melkist.utils.PRICE_FROM_TAG
import com.example.melkist.utils.PRICE_TO_TAG
import com.example.melkist.utils.ROOM_FROM_TAG
import com.example.melkist.utils.ROOM_TO_TAG
import com.example.melkist.utils.SIZE_FROM_TAG
import com.example.melkist.utils.SIZE_TO_TAG
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPersianYear
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import com.google.android.material.textfield.TextInputEditText
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
        viewModel.saveResponse.observe(viewLifecycleOwner) {
            Log.e("TAG", "onViewCreated: ${it.result}")
            Log.e("TAG", "onViewCreated: ${it.message}")
            showDialogWithMessage(
                requireContext(), it.message ?: ""
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                cancel()
            }
        }
    }

    private fun checkFieldsForNullabilitySeparatorAccepted(view: TextInputLayout): Long? {
        if (view.editText!!.text.toString().isNotEmpty() && view.editText!!.text.toString() != "") {
            if (view.editText!!.text.toString()
                    .contains(",")
            ) return BigDecimal(view.editText!!.text.toString().replace(",", "")).toLong()
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
            viewModel.priceFrom = checkFieldsForNullabilitySeparatorAccepted(etPriceFrom)
            viewModel.priceTo = checkFieldsForNullabilitySeparatorAccepted(etPriceTo)
            viewModel.sizeFrom = checkFieldsForNullabilitySeparatorAccepted(etSizeFrom)?.toInt()
            viewModel.sizeTo = checkFieldsForNullabilitySeparatorAccepted(etSizeTo)?.toInt()
            viewmodel?.descriptions = etDescriptions.editText?.text?.toString()
        }
    }

    private fun addLiveSeparator(et: TextInputEditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                et.removeTextChangedListener(this)
                val text: String = p0.toString()
                if (!TextUtils.isEmpty(text)) {
                    val format: String = formatNumber(BigDecimal(text.replace(",", "")).toDouble())
                    et.setText(format)
                    et.setSelection(format.length)
                }
                et.addTextChangedListener(this)
            }
        })
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP6DetailsSeekerFrag_to_addP4LocationFrag)
    }

    fun cancel() {
        requireActivity().finish()
    }

    fun onCommit() {
        gatheringData()
        viewModel.saveFile((activity as AddActivity).user.id!!)
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

    fun onPriceFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_FROM_TAG)
        bottomFrag.setFragmentResultListener(PRICE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainPriceFrom.visibility = View.GONE
                binding.etPriceFromChild.requestFocus()
                addLiveSeparator(binding.etPriceFromChild)
            } else {
                binding.etPriceFromChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toDouble()
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
                addLiveSeparator(binding!!.etPriceToChild)
            } else {
                binding.etPriceToChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toDouble()
                    )
                )
            }
        }
    }
}
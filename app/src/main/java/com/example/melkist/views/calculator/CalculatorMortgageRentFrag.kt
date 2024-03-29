package com.example.melkist.views.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorMortgageRentBinding
import com.example.melkist.utils.ONE_TRILLION
import com.example.melkist.utils.TEN_BILLION
import com.example.melkist.utils.addLiveSeparatorListenerWithNumToLetterCallback
import com.example.melkist.utils.getRemovedSeparatorValue
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.CalculatorViewModel
import com.example.melkist.views.calculator.dialog.CalculatorResultMortgageRentDialog

class CalculatorMortgageRentFrag : Fragment() {

    private lateinit var binding: FragCalculatorMortgageRentBinding
    private val viewModel: CalculatorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorMortgageRentBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@CalculatorMortgageRentFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            onRentToMortgageClick()
            etMortgageChild.addLiveSeparatorListenerWithNumToLetterCallback(
                txtCurrentMortgageToLetter, resources.getString(R.string.tooman)
            )
            etRentChild.addLiveSeparatorListenerWithNumToLetterCallback(
                txtCurrentRentToLetter, resources.getString(R.string.tooman)
            )
            etInputChild.addLiveSeparatorListenerWithNumToLetterCallback(
                txtNewValueToLetter, resources.getString(R.string.tooman)
            )
        }
    }

    private fun resetBtnColorsAndAmounts() {
        binding.apply {
            btnRentToMortgage.setBackgroundColor(resources.getColor(R.color.custom_button_color))
            btnMortgageToRent.setBackgroundColor(resources.getColor(R.color.custom_button_color))
        }
    }

    private fun isFieldsOk():Boolean {
        val a = if (binding.etMortgageChild.getRemovedSeparatorValue() in 0..ONE_TRILLION){
            true
        }else{
            binding.etMortgage.error = resources.getString(R.string.write_right_amount)
            false
        }
        val b = if (binding.etRentChild.getRemovedSeparatorValue() in 0..TEN_BILLION){
            true
        }else{
            binding.etRent.error = resources.getString(R.string.write_right_amount)
            false
        }
        val c = if (binding.etInputChild.getRemovedSeparatorValue() in 0..TEN_BILLION){
            true
        }else{
            binding.etRent.error = resources.getString(R.string.write_right_amount)
            false
        }
        return a && b && c
    }


    /**************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onRentToMortgageClick() {
        viewModel.conditionRentMortgage = viewModel.CONDITION_RENT_TO_MORTGAGE
        resetBtnColorsAndAmounts()
        binding.btnRentToMortgage.setBackgroundColor(resources.getColor(R.color.custom_button_color_selected))
        binding.etInput.hint = resources.getString(R.string.new_rent_value)
    }

    fun onMortgageToRentClick() {
        viewModel.conditionRentMortgage = viewModel.CONDITION_MORTGAGE_TO_RENT
        resetBtnColorsAndAmounts()
        binding.btnMortgageToRent.setBackgroundColor(resources.getColor(R.color.custom_button_color_selected))
        binding.etInput.hint = resources.getString(R.string.new_mortgage_value)
    }

    fun onBtnCalculateClick() {
        binding.apply {
            viewModel.oldMortgageAmount = etMortgageChild.getRemovedSeparatorValue()
            viewModel.oldRentAmount = etRentChild.getRemovedSeparatorValue()
            viewModel.newInputAmount = etInputChild.getRemovedSeparatorValue()
            if(isFieldsOk()){
                viewModel.rentMortgageCalculation(viewModel.conditionRentMortgage)
                if (viewModel.isResultsOk())
                    CalculatorResultMortgageRentDialog(
                        R.style.dialog_theme
                    ).show(
                        childFragmentManager, viewModel.conditionRentMortgage
                    )
                else
                    showDialogWithMessage(requireContext(), resources.getString(R.string.negative_values)) {
                        d,_ -> d.dismiss()
                    }
            }
        }
    }
}
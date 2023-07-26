package com.example.melkist.views.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorCommissionBinding
import com.example.melkist.utils.HUNDRED_MILLION
import com.example.melkist.utils.HUNDRED_THOUSAND
import com.example.melkist.utils.HUNDRED_TRILLION
import com.example.melkist.utils.ONE_MILLION
import com.example.melkist.utils.ONE_QUADRILLION
import com.example.melkist.utils.ONE_TRILLION
import com.example.melkist.utils.addLiveSeparatorListenerWithNumToLetterCallback
import com.example.melkist.utils.getRemovedSeparatorValue
import com.example.melkist.viewmodels.CalculatorViewModel
import com.example.melkist.views.calculator.dialog.CalculatorResultDialogFrag

class CalculatorCommissionFrag : Fragment() {

    private lateinit var binding: FragCalculatorCommissionBinding
    private val viewModel: CalculatorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorCommissionBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@CalculatorCommissionFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBuySaleClick()
    }

    private fun resetBtnColorsAndVisibilitiesAndAmounts() {
        binding.apply {
            btnBuySale.setBackgroundColor(resources.getColor(R.color.custom_button_color))
            btnRent.setBackgroundColor(resources.getColor(R.color.custom_button_color))
            btnMortgage.setBackgroundColor(resources.getColor(R.color.custom_button_color))
            layoutBuySale.visibility = View.GONE
            layoutRent.visibility = View.GONE
            layoutMortgage.visibility = View.GONE
        }
        viewModel.resetAmounts()
    }

    private fun onCalculateBuySale() {
        viewModel.dealAmount = binding.etBuySaleAmountChild.getRemovedSeparatorValue()
        if (viewModel.dealAmount in HUNDRED_MILLION..ONE_QUADRILLION){
            binding.etBuySaleAmount.error = null
            viewModel.calculateBuySaleCommissions()
            CalculatorResultDialogFrag(R.style.dialog_theme).show(
                childFragmentManager,
                viewModel.CONDITION_BUY_SALE
            )
        }else{
            binding.etBuySaleAmount.error = resources.getString(R.string.write_right_amount)
        }
    }

    private fun onCalculateRent() {
        viewModel.rentMortgageAmount = binding.etRentMortgageAmountChild.getRemovedSeparatorValue()
        viewModel.rentRentAmount = binding.etRentRentAmountChild.getRemovedSeparatorValue()
        var a = false
        var b = false
        var c = false
        if (viewModel.rentMortgageAmount in 0..ONE_TRILLION)
            a = true
        else
            binding.etRentMortgageAmount.error = resources.getString(R.string.write_right_amount)
        if (viewModel.rentMortgageAmount in 0..ONE_TRILLION)
            b = true
        else
            binding.etRentRentAmount.error = resources.getString(R.string.write_right_amount)

        if (viewModel.rentMortgageAmount + viewModel.rentMortgageAmount >= HUNDRED_THOUSAND)
            c = true
        else {
            binding.etRentMortgageAmount.error = resources.getString(R.string.write_right_amount)
            binding.etRentRentAmount.error = resources.getString(R.string.write_right_amount)
        }
        if (a && b && c){
            binding.etRentMortgageAmount.error = null
            binding.etRentRentAmount.error = null
            viewModel.calculateRentCommission()
            CalculatorResultDialogFrag(R.style.dialog_theme).show(
                childFragmentManager,
                viewModel.conditionCommission
            )
        }
    }

    private fun onCalculateMortgage() {
        viewModel.mortgageAmount = binding.etMortgageAmountChild.getRemovedSeparatorValue()
        if (viewModel.mortgageAmount in ONE_MILLION..HUNDRED_TRILLION) {
            binding.etMortgageAmount.error = null
            viewModel.calculateMortgageCommission()
            CalculatorResultDialogFrag(R.style.dialog_theme).show(
                childFragmentManager,
                viewModel.CONDITION_MORTGAGE
            )
        } else {
            binding.etMortgageAmount.error = resources.getString(R.string.write_right_amount)
        }
    }

    /***************** binding methods **********************/

    fun back() {
        findNavController().popBackStack()
    }

    fun onCalculateClick() {
        when (viewModel.conditionCommission) {
            viewModel.CONDITION_BUY_SALE -> onCalculateBuySale()
            viewModel.CONDITION_RENT -> onCalculateRent()
            viewModel.CONDITION_MORTGAGE -> onCalculateMortgage()
        }
    }

    fun onBuySaleClick() {
        viewModel.conditionCommission = viewModel.CONDITION_BUY_SALE
        resetBtnColorsAndVisibilitiesAndAmounts()
        binding.btnBuySale.setBackgroundColor(resources.getColor(R.color.custom_button_color_selected))
        binding.layoutBuySale.visibility = View.VISIBLE
        binding.etBuySaleAmountChild
            .addLiveSeparatorListenerWithNumToLetterCallback(
                binding.txtBuySaleToLetter,
                resources.getString(R.string.tooman)
            )
    }

    fun onClearBuySaleClick() {
        binding.etBuySaleAmountChild.text?.clear()
        binding.etBuySaleAmount.error = null
    }

    fun onRentClick() {
        viewModel.conditionCommission = viewModel.CONDITION_RENT
        resetBtnColorsAndVisibilitiesAndAmounts()
        binding.btnRent.setBackgroundColor(resources.getColor(R.color.custom_button_color_selected))
        binding.layoutRent.visibility = View.VISIBLE
        binding.etRentMortgageAmountChild
            .addLiveSeparatorListenerWithNumToLetterCallback(
                binding.txtRentMortgageToLetter,
                resources.getString(R.string.tooman)
            )
        binding.etRentRentAmountChild
            .addLiveSeparatorListenerWithNumToLetterCallback(
                binding.txtRentRentToLetter,
                resources.getString(R.string.tooman)
            )
    }

    fun onClearRentMortgageClick() {
        binding.etRentMortgageAmountChild.text?.clear()
        binding.etRentMortgageAmount.error = null
    }

    fun onClearRentRentClick() {
        binding.etRentRentAmountChild.text?.clear()
        binding.etRentRentAmount.error = null
    }

    fun onMortgageClick() {
        viewModel.conditionCommission = viewModel.CONDITION_MORTGAGE
        resetBtnColorsAndVisibilitiesAndAmounts()
        binding.btnMortgage.setBackgroundColor(resources.getColor(R.color.custom_button_color_selected))
        binding.layoutMortgage.visibility = View.VISIBLE
        binding.etMortgageAmountChild
            .addLiveSeparatorListenerWithNumToLetterCallback(
                binding.txtMortgageToLetter,
                resources.getString(R.string.tooman)
            )
    }

    fun onClearMortgageClick() {
        binding.etMortgageAmountChild.text?.clear()
        binding.etMortgageAmount.error = null
    }
}
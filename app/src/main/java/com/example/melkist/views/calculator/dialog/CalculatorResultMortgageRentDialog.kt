package com.example.melkist.views.calculator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.DialogCalculatorResultMortgageRentBinding
import com.example.melkist.utils.copyToClipboard
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.CalculatorViewModel

class CalculatorResultMortgageRentDialog(
    @StyleRes private val style: Int
) : DialogFragment() {

    private lateinit var binding: DialogCalculatorResultMortgageRentBinding
    private val viewModel: CalculatorViewModel by activityViewModels()
    private val HUNDRED_THOUSAND = 100000
    private var newRentValue = 0L
    private var newMortgageValue = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, style)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogCalculatorResultMortgageRentBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@CalculatorResultMortgageRentDialog
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculation()
        initMortgageRentValue()
        setMinMaxValueOfSeekBar()
        binding.seekbarMortgage.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?, progress: Int, fromUser: Boolean
            ) {
                setMortgageNewNumberSeekbar(progress)
                setRentNewNumberSeekbar(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun calculation() {
        when (tag) {
            viewModel.CONDITION_RENT_TO_MORTGAGE -> {
                newMortgageValue =
                    viewModel.calculateNewMortgageResult(viewModel.newInputAmount).toLong()
                newRentValue = viewModel.newInputAmount
            }

            viewModel.CONDITION_MORTGAGE_TO_RENT -> {
                newMortgageValue = viewModel.newInputAmount
                newRentValue = viewModel.calculateNewRentResult(viewModel.newInputAmount).toLong()
            }
        }
    }

    private fun setMortgageNewNumberSeekbar(progress: Int) {
        binding.txtMortgageNumber.text = String.format(
            "%s %s", formatNumber((progress * HUNDRED_THOUSAND).toDouble()), resources.getString(
                R.string.tooman
            )
        )
    }

    private fun setRentNewNumberSeekbar(progress: Int) {
        binding.txtRentNumber.text = String.format(
            "%s %s",
            formatNumber(viewModel.calculateNewRentResult(progress.toLong() * HUNDRED_THOUSAND).toDouble()),
            resources.getString(
                R.string.tooman
            )
        )
    }

    private fun setMinMaxValueOfSeekBar() {
        binding.apply {
            seekbarMortgage.max = (viewmodel!!.getMaxMortgageValue() / HUNDRED_THOUSAND).toInt()
            seekbarMortgage.progress = (newMortgageValue / HUNDRED_THOUSAND).toInt()
        }
    }

    private fun initMortgageRentValue() {
        binding.apply {
            txtMortgageNumber.text = String.format(
                "%s %s", formatNumber(
                    newMortgageValue.toDouble()
                ), resources.getString(
                    R.string.tooman
                )
            )
            txtRentNumber.text = String.format(
                "%s %s", formatNumber(
                    newRentValue.toDouble()
                ), resources.getString(
                    R.string.tooman
                )
            )
        }
    }

    /** binding methods ******************************/
    fun close() {
        this.dismiss()
    }

    fun resultTitleText(): String {
        return when (tag) {
            viewModel.CONDITION_RENT_TO_MORTGAGE -> resources.getString(R.string.new_mortgage_amount_title)

            viewModel.CONDITION_MORTGAGE_TO_RENT -> resources.getString(R.string.new_rent_amount_title)

            else -> ""
        }
    }

    fun resultText(): String {
        val result = when (tag) {
            viewModel.CONDITION_RENT_TO_MORTGAGE -> newMortgageValue

            viewModel.CONDITION_MORTGAGE_TO_RENT -> newRentValue

            else -> 0
        }
        return String.format(
            "%s %s", formatNumber(result.toDouble()), resources.getString(R.string.tooman)
        )
    }

    fun inputTitleText(): String {
        return when (tag) {
            viewModel.CONDITION_RENT_TO_MORTGAGE -> resources.getString(R.string.new_rent_amount_title)

            viewModel.CONDITION_MORTGAGE_TO_RENT -> resources.getString(R.string.new_mortgage_amount_title)

            else -> ""
        }
    }

    fun inputText(): String {
        return String.format(
            "%s %s",
            formatNumber(viewModel.newInputAmount.toDouble()),
            resources.getString(R.string.tooman)
        )
    }

    fun onResultCopyClick() {
        when (tag) {
            viewModel.CONDITION_RENT_TO_MORTGAGE -> {
                copyToClipboard(
                    requireContext(),
                    viewModel.conditionRentMortgage,
                    newMortgageValue.toString()
                )
            }

            viewModel.CONDITION_MORTGAGE_TO_RENT -> {
                copyToClipboard(
                    requireContext(),
                    viewModel.conditionRentMortgage,
                    newRentValue.toString()
                )
            }
        }
        showToast(
            requireContext(), resources.getString(R.string.added_to_clipboard)
        )
    }
}
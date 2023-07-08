package com.example.melkist.views.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorMortgageRentBinding
import com.example.melkist.utils.addLiveSeparatorListenerWithNumToLetterCallback
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getRemovedSeparatorValue

class CalculatorMortgageRentFrag : Fragment() {

    private lateinit var binding: FragCalculatorMortgageRentBinding
    // TODO: is need view model?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorMortgageRentBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            // TODO: is need view model?
            fragment = this@CalculatorMortgageRentFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etMortgageChild.addLiveSeparatorListenerWithNumToLetterCallback(
                txtCurrentMortgageToLetter, resources.getString(R.string.tooman)
            )
            etRentChild.addLiveSeparatorListenerWithNumToLetterCallback(
                txtCurrentRentToLetter, resources.getString(R.string.tooman)
            )
            btnCalculate.setOnClickListener {

                // TODO: check if rent and mortgage fields are not empty
                showResultPart()
                initMortgageRentValue()
                setMinMaxValueOfSeekBar()
                seekbarMortgage.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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
        }
    }

    private fun setMortgageNewNumberSeekbar(progress: Int) {
        binding.txtMortgageNumber.text = String.format(
            "%s %s", formatNumber(progress.toDouble()), resources.getString(
                R.string.tooman
            )
        )
    }

    private fun setRentNewNumberSeekbar(progress: Int) {
        val newRent: Double = binding.etRentChild.getRemovedSeparatorValue() - (progress / 50 * 1.5)
        binding.txtRentNumber.text = String.format(
            "%s %s", formatNumber(newRent), resources.getString(
                R.string.tooman
            )
        )
    }

    private fun showResultPart() {
        binding.apply {
            txtMortgageNumberTitle.visibility = View.VISIBLE
            txtMortgageNumber.visibility = View.VISIBLE
            txtRentNumberTitle.visibility = View.VISIBLE
            txtRentNumber.visibility = View.VISIBLE
            seekbarMortgage.visibility = View.VISIBLE
            txtDragLeftRight.visibility = View.VISIBLE
        }
    }

    private fun setMinMaxValueOfSeekBar() {
        binding.apply {
            val maxValue = etMortgageChild.getRemovedSeparatorValue()
                .toInt() + (etRentChild.getRemovedSeparatorValue().toDouble() * 50 / 1.5).toInt()
            seekbarMortgage.max = maxValue
            seekbarMortgage.progress = maxValue / 2
        }
    }

    private fun initMortgageRentValue() {
        binding.apply {
            txtMortgageNumber.text = String.format(
                "%s %s", formatNumber(
                    etMortgageChild.getRemovedSeparatorValue().toDouble()
                ), resources.getString(
                    R.string.tooman
                )
            )
            txtRentNumber.text = String.format(
                "%s %s", formatNumber(
                    etRentChild.getRemovedSeparatorValue().toDouble()
                ), resources.getString(
                    R.string.tooman
                )
            )
        }
    }

    /**************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }
}
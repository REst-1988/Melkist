package com.example.melkist.views.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.FragMortgageRentCalculatorBinding
import com.example.melkist.utils.addLiveSeparatorListener
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getRemovedSeparatorValue
import com.example.melkist.viewmodels.MainViewModel

class CalculatorMortgageRentFrag : Fragment() {

    private lateinit var binding: FragMortgageRentCalculatorBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragMortgageRentCalculatorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etMortgageChild.addLiveSeparatorListener()
            etRentChild.addLiveSeparatorListener()
            btnCalculate.setOnClickListener {
                seekbarMortgage.max =
                    binding.etMortgageChild.getRemovedSeparatorValue().toInt() * 2
                seekbarMortgage.progress =
                    binding.etMortgageChild.getRemovedSeparatorValue().toInt()
                seekbarMortgage.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        txtMortgageNumber.text =
                            String.format(
                                "%s %s", formatNumber(progress.toDouble()), resources.getString(
                                    R.string.tooman
                                )
                            )
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
    }
}
package com.example.melkist.views.calculator.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorResultExpertDialogBinding
import com.example.melkist.utils.copyToClipboard
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.numInLetter
import com.example.melkist.utils.showToast

class CalculatorResultExpertDialogFrag(
    @StyleRes private val style: Int,
    private val housePrice: Long,
    private val housePricePerMeter: Long
) : DialogFragment() {

    private lateinit var binding: FragCalculatorResultExpertDialogBinding
    private val WAITING = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, style)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorResultExpertDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            showViews()
        }, WAITING)

        binding.apply {
            btnCloseDialog.setOnClickListener {
                this@CalculatorResultExpertDialogFrag.dismiss()
            }
            ibtnResultCopy.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    resources.getString(R.string.expert_evaluation_house_price),
                    housePrice.toString()
                )
                showToast(
                    requireContext(), resources.getString(R.string.added_to_clipboard)
                )
            }
            ibtnResultCopyPerMeter.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    resources.getString(R.string.expert_evaluation_house_price_per_meter),
                    housePricePerMeter.toString()
                )
                showToast(
                    requireContext(), resources.getString(R.string.added_to_clipboard)
                )
            }
        }
    }

    private fun showViews() {
        binding.apply {
            progress.visibility = View.GONE
            dialogTitle.visibility = View.GONE
            layoutViews.visibility = View.VISIBLE
            txtHousePrice.text = String.format(
                "%s %s",
                formatNumber(housePrice),
                resources.getString(R.string.tooman)
            )
            txtHousePriceToLetter.text = String.format(
                "%s %s",
                numInLetter(requireContext(), housePrice),
                resources.getString(R.string.tooman)
            )
            txtHousePerMeterPrice.text = String.format(
                "%s %s",
                formatNumber(housePricePerMeter),
                resources.getString(R.string.tooman)
            )
            txtHousePricePerMeterToLetter.text = String.format(
                "%s %s",
                numInLetter(requireContext(), housePricePerMeter),
                resources.getString(R.string.tooman)
            )
        }
    }
}
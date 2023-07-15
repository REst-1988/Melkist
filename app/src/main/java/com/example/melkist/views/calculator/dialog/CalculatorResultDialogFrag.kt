package com.example.melkist.views.calculator.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.DialogLayoutCalculationResultBinding
import com.example.melkist.utils.copyToClipboard
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.CalculatorViewModel


class CalculatorResultDialogFrag(@StyleRes private val style: Int) : DialogFragment() {

    private lateinit var binding: DialogLayoutCalculationResultBinding
    private val viewModel: CalculatorViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, style)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLayoutCalculationResultBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@CalculatorResultDialogFrag
        }
        return binding.root
    }

    /*************** binding methods *********************/
    fun onClose() {
        this.dismiss()
    }

    fun realEstateCommissionFeeText(): String {
        return resources.getString(
            R.string.real_estate_commission_fee,
            formatNumber(viewModel.realEstateCommissionFee.toDouble())
        )
    }

    fun onRealEstateCommissionFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.realEstateCommissionFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun txtRealEstateCommissionFeeText(): String {
        return resources.getString(
            R.string.tax_fee, formatNumber(viewModel.realEstateCommissionTaxFee.toDouble())
        )
    }

    fun onRealEstateTaxFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.realEstateCommissionTaxFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun realEstateCommissionTotalText(): String {
        return resources.getString(
            R.string.real_estate_commission_total,
            formatNumber(viewModel.realEstateCommissionTotalFee.toDouble())
        )
    }

    fun onRealEstateTotalFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.realEstateCommissionTotalFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun ownerQuotaFeeText(): String {
        return resources.getString(
            R.string.owner_quota_fee, formatNumber(viewModel.ownerQuotaFee.toDouble())
        )
    }

    fun onOwnerQuotaFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.ownerQuotaFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun ownerQuotaTaxFeeText(): String {
        return resources.getString(
            R.string.tax_fee, formatNumber(viewModel.ownerQuotaTaxFee.toDouble())
        )
    }

    fun onOwnerQuotaTaxFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.ownerQuotaTaxFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun ownerQuotaTotalText(): String {
        return resources.getString(
            R.string.owner_quota_total, formatNumber(viewModel.ownerQuotaTotalFee.toDouble())
        )
    }

    fun onOwnerQuotaTotalFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.ownerQuotaTotalFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun buyerQuotaFeeText(): String {
        return resources.getString(
            R.string.buyer_quota_fee, formatNumber(viewModel.buyerQuotaFee.toDouble())
        )
    }

    fun onBuyerQuotaFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.buyerQuotaFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun buyerQuotaTaxFeeText(): String {
        return resources.getString(
            R.string.tax_fee, formatNumber(viewModel.buyerQuotaTaxFee.toDouble())
        )
    }

    fun onBuyerQuotaTaxFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.buyerQuotaTaxFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }

    fun buyerQuotaTotalText(): String {
        return resources.getString(
            R.string.buyer_quota_total, formatNumber(viewModel.buyerQuotaTotalFee.toDouble())
        )
    }

    fun onBuyerQuotaTotalFeeCopyClick() {
        copyToClipboard(
            requireContext(),
            viewModel.conditionCommission,
            viewModel.buyerQuotaTotalFee.toString()
        )
        showToast(
            requireContext(),
            resources.getString(R.string.added_to_clipboard)
        )
    }
}
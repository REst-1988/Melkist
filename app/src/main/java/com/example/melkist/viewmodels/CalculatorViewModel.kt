package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    val CONDITION_BUY_SALE = "buy_sale"
    val CONDITION_RENT = "rent"
    val CONDITION_MORTGAGE = "mortgage"
    var condition = CONDITION_BUY_SALE

    var dealAmount = 0L
    var rentMortgageAmount = 0L
    var rentRentAmount = 0L
    var mortgageAmount = 0L

    var realEstateCommissionFee = 0L
    var realEstateCommissionTaxFee = 0L
    var realEstateCommissionTotalFee = 0L
    var ownerQuotaFee = 0L
    var ownerQuotaTaxFee = 0L
    var ownerQuotaTotalFee = 0L
    var buyerQuotaFee = 0L
    var buyerQuotaTaxFee = 0L
    var buyerQuotaTotalFee = 0L

    fun calculateBuySaleCommissions() {
        realEstateCommissionFee = (dealAmount * 0.005).toLong()
        realEstateCommissionTaxFee = (realEstateCommissionFee * 0.09).toLong()
        realEstateCommissionTotalFee = realEstateCommissionFee + realEstateCommissionTaxFee

        ownerQuotaFee = realEstateCommissionFee / 2
        ownerQuotaTaxFee = realEstateCommissionTaxFee / 2
        ownerQuotaTotalFee = realEstateCommissionTotalFee / 2
        buyerQuotaFee = realEstateCommissionFee / 2
        buyerQuotaTaxFee = realEstateCommissionTaxFee / 2
        buyerQuotaTotalFee = realEstateCommissionTotalFee / 2
    }

    fun calculateRentCommission() {
        Log.e("TAG", "calculateRentCommission: test", )
        realEstateCommissionFee =
            (rentMortgageAmount * 0.01).toLong() + (rentRentAmount * 0.5).toLong()

        Log.e("TAG", "calculateRentCommission: $realEstateCommissionFee", )
        realEstateCommissionTaxFee = (realEstateCommissionFee * 0.09).toLong()

        Log.e("TAG", "calculateRentCommission: $realEstateCommissionTaxFee", )
        realEstateCommissionTotalFee = realEstateCommissionFee + realEstateCommissionTaxFee

        Log.e("TAG", "calculateRentCommission: $realEstateCommissionTotalFee", )
        ownerQuotaFee = realEstateCommissionFee / 2
        ownerQuotaTaxFee = realEstateCommissionTaxFee / 2
        ownerQuotaTotalFee = realEstateCommissionTotalFee / 2
        buyerQuotaFee = realEstateCommissionFee / 2
        buyerQuotaTaxFee = realEstateCommissionTaxFee / 2
        buyerQuotaTotalFee = realEstateCommissionTotalFee / 2
    }

    fun calculateMortgageCommission() {
        realEstateCommissionFee = (mortgageAmount * 0.01).toLong()
        realEstateCommissionTaxFee = (realEstateCommissionFee * 0.09).toLong()
        realEstateCommissionTotalFee = realEstateCommissionFee + realEstateCommissionTaxFee

        ownerQuotaFee = realEstateCommissionFee / 2
        ownerQuotaTaxFee = realEstateCommissionTaxFee / 2
        ownerQuotaTotalFee = realEstateCommissionTotalFee / 2
        buyerQuotaFee = realEstateCommissionFee / 2
        buyerQuotaTaxFee = realEstateCommissionTaxFee / 2
        buyerQuotaTotalFee = realEstateCommissionTotalFee / 2
    }

    fun resetAmounts() {
        realEstateCommissionFee = 0L
        realEstateCommissionTaxFee = 0L
        realEstateCommissionTotalFee = 0L

        ownerQuotaFee = 0L
        ownerQuotaTaxFee = 0L
        ownerQuotaTotalFee = 0L
        buyerQuotaFee = 0L
        buyerQuotaTaxFee = 0L
        buyerQuotaTotalFee = 0L
    }
}
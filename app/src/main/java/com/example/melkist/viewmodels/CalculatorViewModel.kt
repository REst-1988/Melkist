package com.example.melkist.viewmodels

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    // rent mortgage values //
    var oldMortgageAmount = 0L
    var oldRentAmount = 0L
    var newInputAmount = 0L
    val CONDITION_MORTGAGE_TO_RENT = "MortgageToRent"
    val CONDITION_RENT_TO_MORTGAGE = "rentToMortgage"
    var conditionRentMortgage = CONDITION_RENT_TO_MORTGAGE

    // Commission values //
    val CONDITION_BUY_SALE = "buy_sale"
    val CONDITION_RENT = "rent"
    val CONDITION_MORTGAGE = "mortgage"
    var conditionCommission = CONDITION_BUY_SALE

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

    //////////////////////  rent mortgage calculations /////////////////////////////////////////////
    fun getMaxMortgageValue() = oldMortgageAmount + (oldRentAmount * 50 / 1)

    fun calculateNewRentResult(newMortgageValue: Long) =
        (oldRentAmount) + (((oldMortgageAmount) - newMortgageValue) * 1 / 50)

    fun calculateNewMortgageResult(newRentValue: Long) =
        oldMortgageAmount + ((oldRentAmount - newRentValue) / 1 * 50)

    //////////////////////  commission calculations ////////////////////////////////////////////////
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
        realEstateCommissionFee =
            (rentMortgageAmount * 0.01).toLong() + (rentRentAmount * 0.5).toLong()
        realEstateCommissionTaxFee = (realEstateCommissionFee * 0.09).toLong()
        realEstateCommissionTotalFee = realEstateCommissionFee + realEstateCommissionTaxFee

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
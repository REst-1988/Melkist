package com.example.melkist.utils

import android.content.Context
import com.example.melkist.R
import com.google.android.material.textfield.TextInputLayout



fun isText (context: Context, etText: TextInputLayout?): Boolean {
    if (etText?.editText?.text.isNullOrEmpty()) {
        etText?.error =
            context.resources.getString(R.string.error_on_empty_field)
        return false
    }
    etText?.error = null
    return true
}

fun isPhoneNo(context: Context, etPhone: TextInputLayout?): Boolean {
    // not empty field
    if (etPhone?.editText?.text.isNullOrEmpty()) {
        etPhone?.error =
            context.resources.getString(R.string.error_on_empty_mobile_no)
        return false
    }
    // not wrong field
    else if (etPhone?.editText?.text?.substring(0, 2) != "09" ||
        etPhone.editText!!.text.length != 11
    ) {
        etPhone?.error =
            context.resources.getString(R.string.error_on_wrong_mobile_no)
        return false
    }
    etPhone.error = null
    return true
}

fun isShowAgeField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowSizeField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        4 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        4 -> true
        5 -> true
        6 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowRoomsField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowTotalPriceField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        4 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowMortgageField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowRentField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowSuitableForField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowFloorField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowParkingField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowStoreRoomField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowBalconyField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        2 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowElevatorField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        3 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowAdminDeedField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        3 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        2 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}

fun isShowDeedTypeField(catId: Int, subCatId: Int, typeId: Int? = null): Boolean {
    val typeStat = true
    val catStat = when (catId) {
        1 -> true
        2 -> true
        4 -> true
        else -> false
    }
    val subCatStat = when (subCatId) {
        1 -> true
        2 -> true
        4 -> true
        5 -> true
        6 -> true
        else -> false
    }
    return typeStat && catStat && subCatStat
}
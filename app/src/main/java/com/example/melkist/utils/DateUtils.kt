package com.example.melkist.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.melkist.R
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import saman.zamani.persiandate.PersianDate



fun pickDateDialogForResult(context: Context): LiveData<Long> {
    val result = MutableLiveData<Long>()
    val typeface = Typeface.createFromAsset(context.assets, "iransans.ttf")
    val persianDate = PersianCalendar()
    val picker = PersianDatePickerDialog(context)
        .setPositiveButtonString(context.resources.getString(R.string.confirm))
        .setNegativeButton(context.resources.getString(R.string.close))
        .setPickerBackgroundColor(if (isSystemThemeInDarkMode(context)) Color.DKGRAY else Color.WHITE)
        .setBackgroundColor(if (isSystemThemeInDarkMode(context)) Color.DKGRAY else Color.WHITE)
        .setMinYear(1320)
        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
        .setTitleColor(if (isSystemThemeInDarkMode(context)) Color.WHITE else Color.DKGRAY)
        .setActionTextColor(if (isSystemThemeInDarkMode(context)) Color.WHITE else Color.GRAY)
        .setTypeFace(typeface)
        .setInitDate(persianDate, true)
        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
        .setShowInBottomSheet(true)
        .setListener(object : Listener {
            override fun onDateSelected(persianCalendar: PersianCalendar?) {
                persianCalendar?.apply {
                    Log.e("TAG", "onDateSelected: ${getPersianTimestamp()}", )
                    if (getPersianTimestamp() > timeInMillis)
                        result.value = timeInMillis
                    else
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.picked_date_is_greater),
                            Toast.LENGTH_SHORT
                        ).show()

                }
            }

            override fun onDismissed() {
            }
        })
    picker.show()
    return result
}

fun getTimeStampForLoadImages(): String {
    return System.currentTimeMillis().toString()
}

fun getPersianYear(): Int = PersianDate().shYear

fun getPersianYear(timestamp: Long): Int = PersianDate(timestamp).shYear

fun getPersianMonthNumber (): Int = PersianDate().shMonth

fun getPersianMonthName(timestamp: Long): String = PersianDate(timestamp).monthName

fun getPersianMonthNumber(timestamp: Long): Int = PersianDate(timestamp).shMonth

fun getPersianDate(): String = PersianCalendar().persianShortDate

fun getPersianTimestamp(): Long = PersianCalendar().timeInMillis

fun getStringDateByTimestamp(time: Long): String = PersianCalendar(time).persianShortDate

fun getMonthNameWithMonthNo (monthNo: Int): String {
    return when (monthNo){
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }
}

fun getMonthFromNowToOneYearBefore (monthNo: Int): List<Pair<Int, Int>> {
    val listOfMonthNo = mutableListOf<Pair<Int, Int>>()
    for (i in 0..11){
        val a = monthNo - i
        val b = if(a > 0) 0 else 1
        val c = a + 12 * b
        val year = if(a > 0) getPersianYear() else getPersianYear() - 1
        listOfMonthNo.add(Pair(year, c))
    }
    return listOfMonthNo
}


fun getCorrectDate(rawRealDate: Long): Long {
    return when (rawRealDate.toString().length) {
        10 -> rawRealDate * 1000
        12 -> rawRealDate * 10
        else -> rawRealDate
    }
}
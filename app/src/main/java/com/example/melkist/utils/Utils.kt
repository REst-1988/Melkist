package com.example.melkist.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.melkist.LoginActivity
import com.example.melkist.R
import com.example.melkist.databinding.DialogLayoutGetAddDetailsBinding
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.Period
import com.example.melkist.models.User
import com.example.melkist.network.Api
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import java.math.BigDecimal
import java.text.DecimalFormat

object User {
    lateinit var user: User
    var token: String? = null
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    } else
        return try {
            (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!
                .state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI
            )!!.state == NetworkInfo.State.CONNECTED)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    return false
}

fun showToast(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    Log.e("Tag", "result: $s")
}

fun ImageButton.showFav(isFav: Boolean) {
    if (isFav)
        setImageResource(R.drawable.baseline_bookmark_added_24)
    else
        setImageResource(R.drawable.ic_baseline_bookmark_border_24)
}

fun internetProblemDialog(
    activity: Activity,
    action: (DialogInterface, Int) -> Unit
) {
    val builder = AlertDialog.Builder(activity)
    builder.setMessage(activity.resources.getString(R.string.internet_connection_problem))
        .setCancelable(false)
        .setPositiveButton(activity.resources.getText(R.string.retry), action)
        .setNegativeButton(activity.resources.getString(R.string.quit)) { d, _ ->
            d.dismiss()
            showToast(activity, activity.resources.getString(R.string.try_again_later))
            activity.finish()
        }
        .show()
}

fun showDialogWithMessage(
    context: Context, message: String, action: (DialogInterface, Int) -> Unit
) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(context.resources.getText(R.string.confirm), action).show()
}

fun onRequestFalseResult(activity: Activity, e: List<String>, action: (() -> Any?)) {
    if (e.contains("402")) {
        Log.e("TAG", "onRequestFalseResult: ${activity.localClassName}", )
        loginRequiredDialog(activity)
    } else
        showDialogWithMessage(
            activity,
            concatenateText(e)
        ) { d, _ ->
            d.dismiss()
            action()
        }
}

fun loginRequiredDialog(activity: Activity){
    showDialogWith2Actions(
        activity,
        activity.resources.getString(R.string.login_required_massege),
        { d, _ ->
            d.dismiss()
            activity.startActivity(
                Intent(
                    activity,
                    LoginActivity::class.java
                )
            )
            activity.finish()
        },
        { d, _ ->
            d.dismiss()
        },
        activity.resources.getString(R.string.login),
        activity.resources.getString(R.string.sometime_else)
    )
}

fun showDialogWith2Actions(
    context: Context,
    message: String,
    action: (DialogInterface, Int) -> Unit,
    action2: (DialogInterface, Int) -> Unit,
    onPosOptString: String? = null,
    onNegOptString: String? = null,
) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setCancelable(true)
        .setPositiveButton(onPosOptString ?: context.resources.getText(R.string.yes), action)
        .setNegativeButton(onNegOptString ?: context.resources.getText(R.string.no), action2)
        .show()
}

fun getTimeStampForLoadImages(): String {
    return System.currentTimeMillis().toString()
}

fun getPropertyPeriodsText(
    context: Context,
    period: Period,
    @StringRes titleUnit: Int,
    @StringRes unit: Int
): String {
    var text = ""
    if (period.from == null && period.to == null) {
        text = String.format(
            "%s %s",
            context.resources.getString(titleUnit),
            context.resources.getString(R.string.all_items)
        )
    } else if (period.from != null && period.from == period.to) {
        text = String.format("%s %s", period.from, context.resources.getString(unit))
    } else {
        var from = ""
        var to = ""
        if (period.from != null)
            from = String.format("%s %s", context.resources.getString(R.string.from), period.from)
        if (period.to != null)
            to = String.format("%s %s", context.resources.getString(R.string.to), period.to)
        text = String.format("%s %s %s", from, to, context.resources.getString(unit))
    }
    return text
}

fun calculatePricePerMeter(context: Context, price: Period, size: Period): String {
    val priceTo = (price.to ?: 0).toDouble()
    val priceFrom = (price.from ?: 0).toDouble()
    val sizeTo = (size.to ?: 1).toDouble()
    val sizeFrom = (size.from ?: 1).toDouble()
    return if (price.to == price.from)
        formatNumber((priceFrom / sizeFrom).toLong())
    else
        formatNumber((priceFrom / sizeFrom).toLong()) + " " + context.resources.getString(R.string.to) + " " + formatNumber(
            (priceTo / sizeTo).toLong()
        )
}

fun handleSystemException(
    scope: CoroutineScope,
    from: String?,
    e: Exception? = null,
    t: Throwable? = null
) {
    scope.launch {
        try {
            e?.apply {
                //TODO: Uncomment Api.retrofitService.reportBugToSlack(from + " " + stackTraceToString())
            }
            t?.apply {
                //TODO: Uncomment Api.retrofitService.reportBugToSlack(from + " " + stackTraceToString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    e?.apply {
        printStackTrace()
    }
    t?.apply {
        printStackTrace()
    }
}

fun getPropertyPeriodsPriceText(
    context: Context,
    periodPrice: Period,
    @StringRes titleUnit: Int,
    @StringRes unit: Int
): String {
    var text = ""
    if (periodPrice.from == null && periodPrice.to == null) {
        text = String.format(
            "%s %s",
            context.resources.getString(titleUnit),
            context.resources.getString(R.string.all_items)
        )
    } else if (periodPrice.from != null && periodPrice.from == periodPrice.to) {
        text = String.format(
            "%s %s",
            formatNumber(periodPrice.from),
            context.resources.getString(unit)
        )
    } else {
        var from = ""
        var to = ""
        if (periodPrice.from != null)
            from = String.format(
                "%s %s",
                context.resources.getString(R.string.from),
                formatNumber(periodPrice.from.toLong())
            )
        if (periodPrice.to != null)
            to = String.format(
                "%s %s",
                context.resources.getString(R.string.to),
                formatNumber(periodPrice.to.toLong())
            )
        text = String.format("%s %s %s", from, to, context.resources.getString(unit))
    }
    return text
}

fun concatenateText(texts: List<String>?): String {
    var allErrors = ""
    texts?.apply {
        val iterator: Iterator<String> = iterator()
        while (iterator.hasNext()) {
            allErrors += "${iterator.next()}${if (iterator.hasNext()) ", " else ""}"
        }
    }
    return allErrors
}

fun isSystemThemeDarkMode(context: Context): Boolean {
    val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
}

/*
// adding separator to numbers
fun formatNumber(numberDouble: Double): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(numberDouble)
}
*/

// adding separator to numbers
fun formatNumber(numberLong: Long): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(numberLong)
}

fun TextInputEditText.addLiveSeparatorListener() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            this@addLiveSeparatorListener.removeTextChangedListener(this)
            val text: String = p0.toString()
            if (!TextUtils.isEmpty(text)) {
                val format: String =
                    formatNumber(BigDecimal(text.replace(",", "").replace("٬", "")).toLong())
                this@addLiveSeparatorListener.setText(format)
                this@addLiveSeparatorListener.setSelection(format.length)
            }
            this@addLiveSeparatorListener.addTextChangedListener(this)
        }
    })
}

fun TextInputEditText.addLiveSeparatorListenerWithNumToLetterCallback(tv: TextView, unit: String) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0.isNullOrEmpty()) tv.text = ""
        }

        override fun afterTextChanged(p0: Editable?) {
            try {
                this@addLiveSeparatorListenerWithNumToLetterCallback.removeTextChangedListener(
                    this
                )
                val text: String = p0.toString()
                if (!TextUtils.isEmpty(text)) {
                    val value = BigDecimal(text.replace(",", "").replace("٬", "")).toLong()
                    var format =
                        formatNumber(BigDecimal(text.replace(",", "").replace("٬", "")).toLong())
                    this@addLiveSeparatorListenerWithNumToLetterCallback.setText(format)
                    this@addLiveSeparatorListenerWithNumToLetterCallback.setSelection(format.length)
                    tv.text = String.format(
                        "%s %s", numInLetter(context, value), unit
                    )
                }
                this@addLiveSeparatorListenerWithNumToLetterCallback.addTextChangedListener(this)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    })
}

fun showInputDialog(
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    title: String,
    unit: String?,
    maxLength: Int?,
    observer: Observer<in String?>
) {
    try {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutGetAddDetailsBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        binding.txtTitle.text = String.format("%s (%s)", title, unit)
        binding.etInput.showSoftInputOnFocus
        maxLength?.apply { binding.etInput.filters = arrayOf(InputFilter.LengthFilter(maxLength)) }
        binding.etInput.requestFocus()
        binding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) binding.txtInLetters.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.etInput.removeTextChangedListener(this)
                val text: String = p0.toString()

                if (!TextUtils.isEmpty(text)) {
                    val value: Long = BigDecimal(text.replace(",", "").replace("٬", "")).toLong()
                    val format: String =
                        formatNumber(BigDecimal(text.replace(",", "").replace("٬", "")).toLong())
                    binding.etInput.setText(format)
                    binding.etInput.setSelection(format.length)
                    binding.txtInLetters.text = String.format(
                        "%s %s", numInLetter(context, value), unit
                    )
                }
                binding.etInput.addTextChangedListener(this)
            }
        })
        binding.btnConfirm.setOnClickListener {
            if (binding.etInput.text.isNotEmpty()) {
                if (isConditionsOkForFields(binding.etInput.text.toString())) {
                    result.value = binding.etInput.text.toString().replace(",", "").replace("٬", "")
                    alertDialog.dismiss()
                } else {
                    showToast(
                        context, context.resources.getString(R.string.input_right_number)
                    )
                }
            } else {
                showToast(
                    context,
                    context.resources.getString(R.string.on_empty_dialog_edittext_feild)
                )
            }
        }
        result.observe(viewLifecycleOwner, observer)
    } catch (e: java.lang.Exception) {
        handleSystemException(
            viewLifecycleOwner.lifecycleScope,
            "utils, showInputDialog",
            e
        )
    }
}

fun showAgeDialog(
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    title: String,
    observer: Observer<in String?>
) {
    try {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutGetAddDetailsBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        binding.txtTitle.text = title
        binding.etInput.filters = arrayOf(InputFilter.LengthFilter(4))
        binding.txtInLetters.visibility = View.GONE
        binding.btnConfirm.setOnClickListener {
            if (binding.etInput.text.isNotEmpty()) {
                if (isConditionsOk(binding.etInput.text.toString())) {
                    result.value = binding.etInput.text.toString()
                    alertDialog.cancel()
                } else {
                    showToast(
                        context, context.resources.getString(R.string.input_right_number)
                    )
                }
            } else {
                showToast(
                    context,
                    context.resources.getString(R.string.on_empty_dialog_edittext_feild)
                )
            }
        }
        result.observe(viewLifecycleOwner, observer)
    } catch (e: java.lang.Exception) {
        handleSystemException(viewLifecycleOwner.lifecycleScope, "utils, showAgeDialog", e)
    }
}

private fun isConditionsOk(input: String): Boolean =
    input.toInt() <= getPersianYear() && input.toInt() >= 1150


private fun isConditionsOkForFields(input: String): Boolean =
    input.replace(",", "").replace("٬", "").toLong() > 0

fun TextInputEditText.getRemovedSeparatorValue(): Long {
    if (!this.text.isNullOrBlank())
        return this.text.toString().replace(",", "").replace("٬", "").toLong()
    return 0L
}

fun copyToClipboard(context: Context, title: String, value: String) {
    val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(title, value)
    clipboard.setPrimaryClip(clip)
}

fun changeAppTheme(theme: Int) {
    when (theme) {
        0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

fun getPersianYear(): Int = PersianDate().shYear

fun numInLetter(context: Context, num: Long): String {
    //var result = ""
    var number = num
    var soFar = ""
    val billion = (number / 1000000000).toInt()
    if (billion > 0)
        soFar = "$billion ${context.resources.getString(R.string.billion)}"
    number %= 1000000000
    val million = (number / 1000000).toInt()
    if (million > 0)
        soFar = "$soFar ${
            if (soFar.isNotEmpty())
                context.resources.getString(R.string.and)
            else ""
        } $million ${context.resources.getString(R.string.million)}"
    number %= 1000000
    val thousand = (number / 1000).toInt()
    if (thousand > 0) soFar = "$soFar ${
        if (soFar.isNotEmpty())
            context.resources.getString(R.string.and)
        else ""
    } $thousand ${
        context.resources.getString(R.string.thousend)
    }"
    number %= 1000
    if (number > 0) soFar = "$soFar ${
        if (soFar.isNotEmpty())
            context.resources.getString(R.string.and)
        else ""
    } $number"
    return soFar
}

fun hasFilterData(filerFileData: FilterFileData): Boolean {
    return filerFileData.let { filterFile ->
        filterFile.rooms.from != 0L ||
                filterFile.rooms.to != 0L ||
                filterFile.price.from != 0L ||
                filterFile.price.to != 0L ||
                filterFile.age.from != 0L ||
                filterFile.age.to != 0L ||
                filterFile.size.from != 0L ||
                filterFile.size.to != 0L ||
                filterFile.typeId != null ||
                filterFile.catId != null ||
                filterFile.subCatId != null ||
                filterFile.regionId != null
    }
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



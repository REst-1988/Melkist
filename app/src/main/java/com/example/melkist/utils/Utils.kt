package com.example.melkist.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.InspectableProperty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.models.User
import com.google.android.material.textfield.TextInputEditText
import saman.zamani.persiandate.PersianDate
import java.math.BigDecimal
import java.text.DecimalFormat

object User {
    lateinit var user: User
    var token: String? = null
}

fun isOnline(context: Context): Boolean { //TODO: CHECK INTERNET with my note 3 for make it sure that it worked well
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
    } else {
        return try {
            (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!
                .getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI
            )!!.getState() == NetworkInfo.State.CONNECTED)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    return false
}

fun showToast(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    Log.e("Tag", "result: $s")
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

fun concatenateText(texts: List<String>?): String {
    var allErrors = ""
    if (texts != null) for (error in texts) {
        allErrors += ("$error, ")
    }
    return allErrors
}

fun isSystemDarkMode(context: Context): Boolean {
    val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
}

// adding separator to numbers
fun formatNumber(numberDouble: Double): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(numberDouble)
}
fun TextInputEditText.addLiveSeparatorListener() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            this@addLiveSeparatorListener.removeTextChangedListener(this)
            val text: String = p0.toString()
            if (!TextUtils.isEmpty(text)) {
                val format: String = formatNumber(BigDecimal(text.replace(",", "")).toDouble())
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
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            this@addLiveSeparatorListenerWithNumToLetterCallback.removeTextChangedListener(this)
            val text: String = p0.toString()
            if (!TextUtils.isEmpty(text)) {
                val value: Long = BigDecimal(text.replace(",", "")).toLong()
                val format: String = formatNumber(BigDecimal(text.replace(",", "")).toDouble())
                this@addLiveSeparatorListenerWithNumToLetterCallback.setText(format)
                this@addLiveSeparatorListenerWithNumToLetterCallback.setSelection(format.length)
                tv.text = String.format(
                    "%s %s", numInLetter(context, value), unit
                )
            }
            this@addLiveSeparatorListenerWithNumToLetterCallback.addTextChangedListener(this)
        }
    })
}
@InspectableProperty
fun TextInputEditText.getRemovedSeparatorValue (): Long {
    if (!this.text.isNullOrBlank())
        return this.text.toString().replace(",", "").toLong()
    return 0L
}

fun copyToClipboard (context: Context, title: String, value: String){
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(title, value)
    clipboard.setPrimaryClip(clip)
}

fun getPersianYear(): Int = PersianDate().shYear

// this function also adding separator to numbers but it is old one used in Shanno project
fun addSeparator(numberLong: Long): String {
    val number = numberLong.toString().trim()
    val buffer = StringBuffer(number)
    val a = buffer.reverse().toString().trim()
    var result = ""
    var counter = 0
    for (i in a.indices) {
        if (counter % 3 == 0) {
            result = result + "," + a[i]
            counter = 0
        } else {
            result += a[i]
        }
        counter++
    }
    return StringBuffer(result).reverse().toString().trim { it <= ' ' }
}

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

object Words {

    private val hundredsNames = arrayOf(
        " صد", " دویست", " سیصد", " چهارصد", " پانصد", " ششصد", " هفتصد", " نه صد"
    )
    private val tensNames = arrayOf(
        "", " ده", " بیست", " سی", " چهل", " پنجاه", " شصت", " هفتاد", " هشتاد", " نود"
    )
    private val numNames = arrayOf(
        "",
        " یک",
        " دو",
        " سه",
        " چهار",
        " پنج",
        " شش",
        " هفت",
        " هشت",
        " نه",
        " ده",
        " یازده",
        " دوازده",
        " سیزده",
        " چهارده",
        " پانزده",
        " شانزده",
        " هفده",
        " هجده",
        " نوزده"
    )

    fun convertLessThanOneThousand(number: Int): String {
        var number = number
        var soFar: String
        if (number % 100 < 20) {
            soFar = numNames[number % 100]
            number /= 100
        } else {
            soFar = numNames[number % 10]
            number /= 10
            soFar = tensNames[number % 10] + soFar
            number /= 10
        }
        return if (number == 0) soFar else numNames[number] + " صد" + soFar
    }

    fun convert(number: Long): String {
        // 0 to 999 999 999 999
        if (number == 0L) {
            return "صفر"
        }
        var snumber = java.lang.Long.toString(number)

        // pad with "0"
        val mask = "000000000000"
        val df = DecimalFormat(mask)
        snumber = df.format(number)

        // XXXnnnnnnnnn
        val billions = snumber.substring(0, 3).toInt()
        // nnnXXXnnnnnn
        val millions = snumber.substring(3, 6).toInt()
        // nnnnnnXXXnnn
        val hundredThousands = snumber.substring(6, 9).toInt()
        // nnnnnnnnnXXX
        val thousands = snumber.substring(9, 12).toInt()
        val tradBillions: String = when (billions) {
            0 -> ""
            1 -> (convertLessThanOneThousand(billions) + " میلیارد ")
            else -> (convertLessThanOneThousand(billions) + " میلیارد ")
        }
        var result = tradBillions
        val tradMillions: String = when (millions) {
            0 -> ""
            1 -> (convertLessThanOneThousand(millions) + " میلیون ")
            else -> (convertLessThanOneThousand(millions) + " میلیون ")
        }
        result += tradMillions
        val tradHundredThousands: String = when (hundredThousands) {
            0 -> ""
            1 -> "یک هزار "
            else -> (convertLessThanOneThousand(hundredThousands) + " هزار ")
        }
        result += tradHundredThousands
        val tradThousand: String = convertLessThanOneThousand(thousands)
        result += tradThousand

        // remove extra spaces!
        return result.replace("^\\s+".toRegex(), "").replace("\\b\\s{2,}\\b".toRegex(), " ")
    }
}



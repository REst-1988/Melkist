package com.example.melkist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.CountDownTimer
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputEditText

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
                .getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
                        .getState() == NetworkInfo.State.CONNECTED
                    )
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    return false
}

fun showToast(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

fun showHidePass(et: TextInputEditText, isShowPass: Boolean) {
    et.inputType =
        if (isShowPass)
            InputType.TYPE_CLASS_TEXT
        else
            InputType.TYPE_TEXT_VARIATION_PASSWORD
}

/*
fun concatenateErrors(verificationResponse: VerificationResponse?): String {
    var allErrors = ""
    if (verificationResponse!= null)
        for (error in verificationResponse!!.errors?: arrayOf()) {
            allErrors += error
        }
    return allErrors
}*/

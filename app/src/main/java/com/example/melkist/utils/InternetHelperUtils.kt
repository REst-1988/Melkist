package com.example.melkist.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import com.example.melkist.R
import com.example.melkist.models.Charts

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

fun onRequestFalseResult(activity: Activity, e: List<String>, action: (() -> Any?)) {
    if (e.contains("402")) {
        Log.e("TAG", "onRequestFalseResult: ${activity.localClassName}")
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
package com.example.melkist.utils

import com.example.melkist.network.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun handleSystemException(
    scope: CoroutineScope,
    from: String?,
    e: Exception? = null,
    t: Throwable? = null
) {
    scope.launch {
        try {
            e?.apply {
                Api.retrofitService.reportBugToSlack(from + " " + stackTraceToString())
            }
            t?.apply {
                Api.retrofitService.reportBugToSlack(from + " " + stackTraceToString())
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
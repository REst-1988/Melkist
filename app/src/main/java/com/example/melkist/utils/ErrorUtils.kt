package com.example.melkist.utils

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
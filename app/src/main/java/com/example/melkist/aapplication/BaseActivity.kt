package com.example.melkist.aapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log


class BaseActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            Log.e(
                "Alert",
                "Lets See if it Works !!!"
            )
        }
    }
}
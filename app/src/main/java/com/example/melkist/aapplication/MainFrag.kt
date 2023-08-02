package com.example.melkist.aapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment


open class MainFrag: Fragment() {

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
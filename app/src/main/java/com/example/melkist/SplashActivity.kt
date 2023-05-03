package com.example.melkist

import android.content.Intent
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

//TODO: checking the version of application
// TODO: check internet connection
// TODO: check if user would sign in before

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000 // 3 seconds
    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mHandler = Handler(Looper.getMainLooper())
        mHandler!!.postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                this@SplashActivity.finish()
                overridePendingTransition(0, 0)
            }, splashTimeOut)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending messages from the handler to avoid memory leaks
        mHandler?.removeCallbacksAndMessages(null)
    }
}
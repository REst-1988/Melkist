package com.example.melkist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.melkist.data.UserDataStore
import com.example.melkist.models.User

//TODO: checking the version of application
// TODO: check internet connection
// TODO: check if user would sign in before

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000
    private var mHandler: Handler? = null
    private lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userDataStore = UserDataStore(this)
        mHandler = Handler(Looper.getMainLooper())
        mHandler!!.postDelayed(
            {
                proceedRunning()
            }, splashTimeOut)
    }

    private fun proceedRunning() {
        userDataStore.preferenceFlow.asLiveData().observe(this) { value ->
            Log.e("TAG", "proceedRunning: $value  ,  ${value.id}  ${value.profilePic}", )
            checkSituation(value)
        }
    }

    private fun checkSituation(user: User?) {
        if (user?.id != null &&  user.id > 0 && !user.isFirstTime!! && user.profilePic != "") {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            this@SplashActivity.finish()
        } else {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            this@SplashActivity.finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending messages from the handler to avoid memory leaks
        mHandler?.removeCallbacksAndMessages(null)
    }
}
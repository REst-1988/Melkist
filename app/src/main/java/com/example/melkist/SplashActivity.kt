package com.example.melkist

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.melkist.data.Ds
import com.example.melkist.data.OptionsDs
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.ActivitySplashScreenBinding
import com.example.melkist.models.User
import com.example.melkist.utils.changeAppTheme
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.SplashViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

//TODO: checking the version of application
// TODO: check internet connection
// TODO: check if user would sign in before

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 100 // TODO: change this to 3000
    private lateinit var binding: ActivitySplashScreenBinding
    private var mHandler: Handler? = null
    private lateinit var userDataStore: UserDataStore
    private var appVersion: String = ""
    private val viewModel: SplashViewModel by viewModels()
    private var user: User? = null

    //1 get user data  DONE
    //2 get firebase token  DONE
    //3 check app version   DONE
    //4 if version not ok download app
    // 5 if version ok check firebase token
    // 6 if firebase token not ok login
    // 7 if ok login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this))
        binding.apply {
            lifecycleOwner = this@SplashActivity
            viewmodel = viewModel
        }
        setContentView(binding.root)
        try {
            checkAppVersion()
            userDataStore = Ds.getDataStore(this)
            listenToAppVersionResponse()
            userDataStore.preferenceFlow.asLiveData().observe(this) { value ->
                user = value
                Log.e("SplashActivity", "onCreate: user = $value")
                checkFirebaseTokenAndProceed()
            }
            OptionsDs.getDataStore(this).themePreferenceFlow.asLiveData().observe(this) {
                Log.e("TAG", "onCreate: theme = $it")
                it?.let { theme -> changeAppTheme(theme) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listenToAppVersionResponse() {
        viewModel.appVersionResponse.observe(this) { response ->
            response?.apply {
                when (versionResult) {
                    true -> createDelay(firebaseTokenResult)
                    false -> downloadLastVersion()
                    else -> showDialogWithMessage(
                        this@SplashActivity,
                        resources.getString(R.string.somthing_goes_wrong)
                    ) { d, _ ->
                        d.dismiss()
                        this@SplashActivity.finish()
                    }
                }
            }
        }
    }

    private fun checkFirebaseTokenResponse(firebaseTokenResult: Boolean?) {
        when (firebaseTokenResult) {
            true -> startMainActivityOnTrueResult()
            false -> startLoginActivityOnFalseResult()
            else -> showDialogWithMessage(
                this@SplashActivity,
                resources.getString(R.string.somthing_goes_wrong)
            ) { d, _ ->
                d.dismiss()
                this@SplashActivity.finish()
            }
        }
    }

    private fun startMainActivityOnTrueResult() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        this@SplashActivity.finish()
    }

    private fun startLoginActivityOnFalseResult() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        this@SplashActivity.finish()
        overridePendingTransition(0, 0)
    }

    private fun checkFirebaseTokenAndProceed() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result // Get new FCM registration token
            Log.e(ContentValues.TAG, token)
            viewModel.callServerAppVersion(userId = user?.id, token, appVersion)
        })
    }

    private fun checkAppVersion() {
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        appVersion = pInfo.versionName
        binding.txtVesrion.text = String.format(resources.getString(R.string.version), appVersion)
    }

    private fun createDelay(firebaseTokenResult: Boolean?) {
        mHandler = Handler(Looper.getMainLooper())
        mHandler!!
            .postDelayed(
                {
                    checkFirebaseTokenResponse(firebaseTokenResult)
                }, splashTimeOut
            )
    }

//    private fun checkSituation(user: User?) {  // TODO: deprecated method no need delete if not necessary
//        if (user?.id != null
//            && user.id > 0
//            && !user.isFirstTime!!
//            && user.profilePic != ""
//        ) {
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            this@SplashActivity.finish()
//        } else {
//            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
//            this@SplashActivity.finish()
//            overridePendingTransition(0, 0)
//        }
//    }

    private fun downloadLastVersion() {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.site_url)))
        startActivity(browserIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending messages from the handler to avoid memory leaks
        mHandler?.removeCallbacksAndMessages(null)
    }
}
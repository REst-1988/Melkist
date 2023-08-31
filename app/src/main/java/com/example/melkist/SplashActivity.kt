package com.example.melkist

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.lifecycle.lifecycleScope
import com.example.melkist.data.Ds
import com.example.melkist.data.OptionsDs
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.ActivitySplashScreenBinding
import com.example.melkist.models.AppVersionResponse
import com.example.melkist.models.User
import com.example.melkist.utils.changeAppTheme
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.SplashViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userDataStore: UserDataStore
    private var appVersion: String = ""
    private val viewModel: SplashViewModel by viewModels()
    private var user: User? = null
    private var firebaseToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this))
            binding.apply {
                lifecycleOwner = this@SplashActivity
                viewmodel = viewModel
            }
            setContentView(binding.root)
            checkAppVersion()
            userDataStore = Ds.getDataStore(this)
            listenToAppVersionResponse()
            userDataStore.preferenceFlow.asLiveData().observe(this) { value ->
                user = value
                Log.e("SplashActivity", "onCreate: userDataStore = $value")
                checkFirebaseTokenAndProceed()
            }
            OptionsDs.getDataStore(this).themePreferenceFlow.asLiveData().observe(this) {
                Log.e("TAG", "onCreate: OptionsDs theme = $it")
                it?.let { theme -> changeAppTheme(theme) }
            }
        } catch (e: Exception) {
            handleSystemException(lifecycleScope, "${user?.id}, SplashActivity, onCreate ", e)
        }
    }

    private fun listenToAppVersionResponse() {
        try {
            viewModel.appVersionResponse.observe(this) { response ->
                response?.apply {
                    when (result) {
                        true -> onTrueAppVersionResult(response)
                        false -> onFalseAppVersionResult(response.errors)
                        else -> showDialogWithMessage(
                            this@SplashActivity,
                            resources.getString(R.string.somthing_goes_wrong)
                        ) { d, _ ->
                            d.dismiss()
                            viewModel.callServerAppVersion(
                                this@SplashActivity,
                                userId = user?.id,
                                firebaseToken,
                                appVersion
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            handleSystemException(
                lifecycleScope,
                "${user?.id}, SplashActivity, listenToAppVersionResponse ",
                e
            )
        }
    }

    private fun onTrueAppVersionResult(response: AppVersionResponse) {
        if (response.versionResult == false) {
            downloadLastVersion()
        } else {
            if (response.firebaseTokenResult == true && response.isFirstTime == false)
                startMainActivityOnTrueResult()
            else
                startLoginActivityOnFalseResult()
        }
    }

    private fun onFalseAppVersionResult(errors: List<String>) {
        showDialogWithMessage(
            this@SplashActivity,
            concatenateText(errors)
        ) { d, _ ->
            d.dismiss()
            lifecycleScope.launch {
                Ds.getDataStore(this@SplashActivity).emptyPreferences()
            }
            startLoginActivityOnFalseResult()
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
            firebaseToken = task.result // Get new FCM registration token
            Log.e(ContentValues.TAG, firebaseToken ?: "")
            Handler(Looper.getMainLooper())
                .postDelayed(
                    {
                        viewModel.callServerAppVersion(
                            this,
                            userId = user?.id,
                            firebaseToken,
                            appVersion
                        )
                    }, splashTimeOut
                )
        })
    }

    private fun checkAppVersion() {
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        appVersion = pInfo.versionName
        binding.txtVesrion.text = String.format(resources.getString(R.string.version), appVersion)
    }

    private fun downloadLastVersion() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(resources.getString(R.string.download_last_version))
                .setCancelable(true)
                .setPositiveButton(resources.getText(R.string.go_to_site)) { d, _ ->
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(resources.getString(R.string.site_url_download))
                        )
                    startActivity(browserIntent)
                }
                .setNegativeButton(resources.getText(R.string.sometime_else)) { d, _ ->
                    d.dismiss()
                    finish()
                }
                .show()

        } catch (e: Exception) {
            handleSystemException(
                lifecycleScope,
                "${user?.id}, SplashActivity, downloadLastVersion ",
                e
            )
        }
    }
}
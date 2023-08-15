package com.example.melkist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.melkist.data.Ds
import com.example.melkist.databinding.ActivityMainBinding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.User
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Interaction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    var user: User? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            /*        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
                        handleSystemException(lifecycleScope, "MainActivity, setDefaultUncaughtExceptionHandler, ", null, paramThrowable)
                        finish()
            }*/
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            navView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_map, R.id.navigation_profle, R.id.navigation_fav
                )
            )
            navView.itemIconTintList = null
            // setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
            navView.selectedItemId = R.id.navigation_add
            binding.ibtnAdd.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        AddActivity::class.java
                    )
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                        if (!it)
                            showToast(
                                this,
                                resources.getString(R.string.permission_no_garent)
                            )
                    }
            }
        } catch (e: Exception) {
            handleSystemException(lifecycleScope, "${user?.id}, MainActivity, onCreate, ", e)
        }
    }

    override fun onResume() {
        try {
            super.onResume()
            if (user == null) {
                val userDataStore = Ds.getDataStore(this)
                userDataStore.preferenceFlow.asLiveData().observe(this) {
                    user = it
                }
            }
            checkNotificationPermission()
        } catch (e: Exception) {
            handleSystemException(lifecycleScope, "${user?.id}, MainActivity, onResume, ", e)
        }
    }

    //////////// helper methods ///////////
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //showPermissionDialog()
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    /*    private fun showPermissionDialog() {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(
                resources.getString(R.string.request_notification_permission))
                .setCancelable(true)
                .setPositiveButton(resources.getText(R.string.allow), action1)
                .setNegativeButton(resources.getText(R.string.not_allow), action2)
                .show()
            )
        }*/

    override fun changBottomNavViewVisibility(visibility: Int) {
        navView.visibility = visibility
        binding.ibtnAdd.visibility = visibility
    }
}
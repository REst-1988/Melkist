package com.example.melkist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.melkist.utils.handleSystemException

class LoginActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
/*        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            handleSystemException(lifecycleScope, "LoginActivity, setDefaultUncaughtExceptionHandler, ", null, paramThrowable)
            finish()
        }*/
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
        } catch (e: Exception){
            handleSystemException(lifecycleScope, "LoginActivity, onCreate, ", e)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
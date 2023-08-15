package com.example.melkist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.melkist.data.UserDataStore
import com.example.melkist.models.User
import com.example.melkist.utils.handleSystemException

class AddActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_add)
            val userDataStore = UserDataStore(this)
            userDataStore.preferenceFlow.asLiveData().observe(this) {
                user = it
            }
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
        } catch (e: Exception){
            handleSystemException(lifecycleScope, "${user?.id}, AddActivity, onCreate, ", e)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
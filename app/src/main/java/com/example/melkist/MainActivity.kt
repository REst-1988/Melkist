package com.example.melkist

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.ActivityMainBinding
import com.example.melkist.models.User
import com.example.melkist.viewmodels.MapViewModel
import com.example.melkist.views.map.MapP1Frag

class MainActivity : AppCompatActivity(), MapP1Frag.Interaction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userDataStore = UserDataStore(this)
        userDataStore.preferenceFlow.asLiveData().observe(this){
            user = it
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun changBottomNavViewVisibility(visibility: Int) {
        navView.visibility = visibility
    }
}
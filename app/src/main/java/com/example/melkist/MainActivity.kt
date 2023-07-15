package com.example.melkist

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.melkist.data.Ds
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.ActivityMainBinding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.User
import com.example.melkist.views.fav.FavListFrag
import com.example.melkist.views.map.MapP1Frag
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), Interaction{

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onResume() {
        super.onResume()
        if (user == null) {
            val userDataStore = Ds.getDataStore(this)
            userDataStore.preferenceFlow.asLiveData().observe(this) {
                user = it
            }
        }
    }

    override fun changBottomNavViewVisibility(visibility: Int) {
        navView.visibility = visibility
        binding.ibtnAdd.visibility = visibility
    }
}
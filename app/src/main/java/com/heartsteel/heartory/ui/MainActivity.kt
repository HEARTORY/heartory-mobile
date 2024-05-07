package com.heartsteel.heartory.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.helper.heartbeat.HeartbeatActivity
import com.heartsteel.heartory.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupBottomNav();
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(_binding.navView.id)
        val fabHeartRate: FloatingActionButton = findViewById(_binding.fabHeartRate.id)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        fabHeartRate.setOnClickListener{
            navController.navigate(R.id.heartRateFragment)
        }
    }
}
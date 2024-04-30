package com.heartsteel.heartory.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.helper.heartbeat.HeartbeatActivity
import com.heartsteel.heartory.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupBottomNav();
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(_binding.navView.id)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }



}
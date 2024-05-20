package com.heartsteel.heartory.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.ActivityMainBinding
import com.heartsteel.heartory.ui.heart_rate.heartbeat.HeartRateActivity
import com.heartsteel.heartory.ui.heart_rate_onboarding.OnBoardingActivity
import com.heartsteel.heartory.ui.heart_rate_onboarding.SharedPreferencesManager
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
           checkAndHandleFirstTime()
        }
    }

//    private fun isFirstTime (){
//        Handler(Looper.getMainLooper()).postDelayed({
//            val sharedPreferencesManager = SharedPreferencesManager(this)
//            Log.d("isFirstTime", sharedPreferencesManager.isFirstTime.toString())
//            if(sharedPreferencesManager.isFirstTime){
//                startActivity(Intent(this, OnBoardingActivity::class.java))
//                finish()
//            }
//        }, 2000)
//    }

    private fun checkAndHandleFirstTime() {
        val sharedPreferencesManager = SharedPreferencesManager(this)
        Log.d("isFirstTime", sharedPreferencesManager.isFirstTime.toString())
        if (sharedPreferencesManager.isFirstTime) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        } else {
            val intent = Intent(this, HeartRateActivity::class.java)
            startActivity(intent)
        }
    }
}
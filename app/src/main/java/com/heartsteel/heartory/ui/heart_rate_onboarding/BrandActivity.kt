package com.heartsteel.heartory.ui.heart_rate_onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heartsteel.heartory.R
import com.heartsteel.heartory.ui.MainActivity

class BrandActivity : AppCompatActivity() {

    //splash screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       setContentView(R.layout.activity_brand)
        isFirstTime()

    }

    override fun onStart() {
        super.onStart()
        isFirstTime()
    }
    private fun isFirstTime (){
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferencesManager = SharedPreferencesManager(this)
            if(sharedPreferencesManager.isFirstTime){
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            finish()
        }, 2000)
    }
}
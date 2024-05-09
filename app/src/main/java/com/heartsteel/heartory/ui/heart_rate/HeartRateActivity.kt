package com.heartsteel.heartory.ui.heart_rate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.ActivityHeartRateBinding

class HeartRateActivity : AppCompatActivity() {
    private val binding: ActivityHeartRateBinding by lazy {
        ActivityHeartRateBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        binding.apply {
//            topAppBar.setNavigationOnClickListener(View.OnClickListener {
//                onBackPressed()
//            })
//        }
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
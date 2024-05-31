package com.heartsteel.heartory.ui.profile_onboarding

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.ActivityProfileOnBoardingBinding
import com.heartsteel.heartory.ui.heart_rate_onboarding.gone
import com.heartsteel.heartory.ui.heart_rate_onboarding.visible

class ProfileOnBoardingActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileOnBoardingBinding
    lateinit var onBoardingViewPager2 : ViewPager2
    lateinit var nextBtn : Button

    private val onBoardingPageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBoardingViewPager2 = findViewById(R.id.profile_viewPager2)

        onBoardingViewPager2.apply {
            adapter = ProfileAdapter(this@ProfileOnBoardingActivity )
            registerOnPageChangeCallback(onBoardingPageChangeCallBack)}
        nextBtn = findViewById(R.id.next_btn)

        nextBtn.setOnClickListener {

        }
    }
}
package com.heartsteel.heartory.ui.heart_rate_onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.ActivityOnBoardingBinding
import com.heartsteel.heartory.ui.heart_rate.HeartRateActivity

class OnBoardingActivity : AppCompatActivity() {

    private val onBoardingPageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            when(position){
                0 -> {
                    skipBtn.text = "Skip"
                    skipBtn.visible()
                    nextBtn.visible()
                    previousBtn.gone()
                }
                pagerList.size - 1 ->{
                    skipBtn.text = "Get Started"
                    skipBtn.gone()
                    nextBtn.visible()
                    previousBtn.visible()
                }
                else -> {
                    skipBtn.text = "Skip"
                    skipBtn.visible()
                    nextBtn.visible()
                    previousBtn.visible()
                }
            }

        }
    }

    private lateinit var binding: ActivityOnBoardingBinding
    private val pagerList = arrayListOf(

        Page(
            "",
            "",
            R.drawable.mia_examine
        ),
        Page(
            "What is blood pressure?",
            """
        Blood pressure is the measure of blood force against artery walls, noted by two numbers: systolic (during heartbeats) and diastolic (between beats), ideally around 120/80 mmHg.
        
        High pressure strains the heart, increasing risks like heart disease; low pressure may cause dizziness. Lifestyle changes, like diet and exercise, help maintain healthy levels. Regular monitoring is key for early detection and prevention of complications, promoting overall heart health.
        """.trimIndent(),
            R.drawable.blood_pressure_1
        ),
        Page(
            "How we measure your blood pressure?",
            """
        Photoplethysmography (PPG) sensors use light to detect changes in blood volume in peripheral blood vessels. As the heart beats, blood flow causes fluctuations in light absorption or reflection.
        
        By analyzing these changes, PPG sensors accurately measure heart rate. This non-invasive method is commonly found in wearable devices for real-time monitoring of heart rate during activities.
        """.trimIndent(),
            R.drawable.blood_pressure_2
        )
    )


    lateinit var onBoardingViewPager2 : ViewPager2
    lateinit var skipBtn : Button
    lateinit var nextBtn : Button
    lateinit var previousBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBoardingViewPager2 = findViewById(R.id.on_boarding_view_pager_2)
        skipBtn = findViewById(R.id.skip_btn)
        nextBtn = findViewById(R.id.next_btn)
        previousBtn  = findViewById(R.id.previous_btn)

        onBoardingViewPager2.apply {
            adapter = OnBoardingAdapter(this@OnBoardingActivity, pagerList)
            registerOnPageChangeCallback(onBoardingPageChangeCallBack)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
//        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_onBoarding)
//        TabLayoutMediator(tabLayout, onBoardingViewPager2) { tab, position-> }.attach()

        nextBtn.setOnClickListener{
            if(onBoardingViewPager2.currentItem < pagerList.size - 1){
                onBoardingViewPager2.currentItem = onBoardingViewPager2.currentItem + 1
            }else{
                heartRateScreenIntent()
            }
        }

        skipBtn.setOnClickListener {

        }

        previousBtn.setOnClickListener {
            if(onBoardingViewPager2.currentItem > 0){
                onBoardingViewPager2.currentItem = onBoardingViewPager2.currentItem - 1
            }
        }
    }

    override fun onDestroy() {
        onBoardingViewPager2.unregisterOnPageChangeCallback(onBoardingPageChangeCallBack)
        super.onDestroy()
    }

    private fun heartRateScreenIntent(){
        val sharedPreferences= SharedPreferencesManager(this)
        sharedPreferences.isFirstTime = false
        val heartRateIntent = Intent(this, HeartRateActivity::class.java)
        startActivity(heartRateIntent)
        finish()
    }
}
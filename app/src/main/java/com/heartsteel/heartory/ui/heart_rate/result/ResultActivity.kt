package com.heartsteel.heartory.ui.heart_rate.result

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.ActivityResultBinding

enum class EMOTION {
    NULL,
    JOY,
    HAPPY,
    SAD,
    CRY,
    NORMAL
}
class ResultActivity : BaseActivity() {
    private val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener{
            finish()
        }

        // Retrieve the pulse value from the Intent extras
        val pulseValue = intent.getStringExtra("PULSE_VALUE")
        val emotion = intent.getStringExtra("SELECTED_EMOTION")
        when(emotion){
            EMOTION.JOY.toString() -> binding.moodIcon.setImageResource(R.drawable.joyful)
            EMOTION.HAPPY.toString() -> binding.moodIcon.setImageResource(R.drawable.happy)
            EMOTION.SAD.toString() -> binding.moodIcon.setImageResource(R.drawable.sad)
            EMOTION.CRY.toString() -> binding.moodIcon.setImageResource(R.drawable.cry)
            EMOTION.NORMAL.toString() -> binding.moodIcon.setImageResource(R.drawable.normal)
        }
        // Update the TextView with the pulse value
        binding.tvPulseValue.text = pulseValue

    }
}
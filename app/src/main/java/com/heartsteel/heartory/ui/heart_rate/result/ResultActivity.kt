package com.heartsteel.heartory.ui.heart_rate.result

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.text.toLowerCase
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.ActivityResultBinding
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ResultActivity : BaseActivity() {
    private val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }
    private var user: User? = null
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Retrieve the pulse value from the Intent extras
        val pulseValue = intent.getStringExtra("PULSE_VALUE")
        val emotion = intent.getStringExtra("SELECTED_EMOTION")
        when (emotion) {
            EMOTION.JOY.toString() -> binding.moodIcon.setImageResource(R.drawable.joyful)
            EMOTION.HAPPY.toString() -> binding.moodIcon.setImageResource(R.drawable.happy)
            EMOTION.SAD.toString() -> binding.moodIcon.setImageResource(R.drawable.sad)
            EMOTION.CRY.toString() -> binding.moodIcon.setImageResource(R.drawable.cry)
            EMOTION.NORMAL.toString() -> binding.moodIcon.setImageResource(R.drawable.normal)
        }
        // Update the TextView with the pulse value
        binding.tvPulseValue.text = pulseValue
        user = viewModel.getUserFromSharePref()
        val age = user?.dateOfBirth?.let { viewModel.getAge(it) }.toString()
        val gender = user!!.gender

        binding.tvAgeValue.text = age
        binding.tvGenderUnit.text = gender
        when(gender!!.toLowerCase()){
            "male" ->{
                binding.imgGender.setImageResource(R.drawable.male)
            }
            "female" -> {
                binding.imgGender.setImageResource(R.drawable.female)
            }
        }

        setupEvents()
        setupObservers()

    }

    private fun setupEvents() {
        binding.btnDiagnose.setOnClickListener {
            binding.tvDiagnoses.text = "Diagnostic in progress..."
            val intRegex = Regex("[0-9]*")
            val bpm = 80
            val diagnosisReq = DiagnosesReq(
                bpm,
                bpm,
                30,
                "7369607740777529345",
                "Now, my heart rate is ${bpm} bpm, my gender is male, my age is 30 years old," +
                        " my height is 170 cm, my weight is 70 kg, " +
                        " I am currently resting, " +
                        " my mood is ${intent.getStringExtra("SELECTED_EMOTION")}, " +
                        ". Can you diagnose me?"
            )

            CoroutineScope(Dispatchers.IO).launch {
                val str = StringBuilder()
                viewModel.getDiagnoses(diagnosisReq).collect {
                    withContext(Dispatchers.Main) {
                        str.append(it.message?.content ?: "")
                        binding.tvDiagnoses.text = str.toString()
                    }
                }
            }
        }

    }

    private fun setupObservers() {
//        viewModel.diagnosisResult.observe(this) {
//            when (it) {
//                is Resource.Loading -> {
//                    binding.tvDiagnoses.text = "Diagnostic in progress..."
//                }
//
//                is Resource.Success -> {
//                    val stringBuilder = StringBuilder()
//                    stringBuilder.append(binding.tvDiagnoses.text.toString())
//                    stringBuilder.append(it.data?.message?.content.toString())
//                    binding.tvDiagnoses.text = stringBuilder.toString()
//                }
//
//                is Resource.Error -> {
//                    binding.tvDiagnoses.text = it.message
//                }
//            }
//        }

    }

    enum class EMOTION {
        NULL,
        JOY,
        HAPPY,
        SAD,
        CRY,
        NORMAL
    }
}
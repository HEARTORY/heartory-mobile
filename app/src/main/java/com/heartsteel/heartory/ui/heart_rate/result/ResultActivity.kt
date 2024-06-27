package com.heartsteel.heartory.ui.heart_rate.result

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.text.toLowerCase
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.ActivityResultBinding
import com.heartsteel.heartory.service.model.domain.HBRecord
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import com.heartsteel.heartory.service.repository.UserRepository
import com.heartsteel.heartory.ui.subscription.SubscriptionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class ResultActivity : BaseActivity() {
    private val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    private var user: User? = null
    private val viewModel: ResultViewModel by viewModels()
    var roundedPulseValue: Int = 0
    lateinit var age: String
    lateinit var gender :String
    var height: Double = 0.0
    var formattedHeight: String = ""
    lateinit var weight :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Retrieve the pulse value from the Intent extras
        val pulseValue = intent.getStringExtra("PULSE_VALUE")
        roundedPulseValue = pulseValue?.toDouble()?.let { roundToNearestInt(it) }!!
        val emotion = intent.getStringExtra("SELECTED_EMOTION")
        when (emotion) {
            EMOTION.JOY.toString() -> binding.moodIcon.setImageResource(R.drawable.joyful)
            EMOTION.HAPPY.toString() -> binding.moodIcon.setImageResource(R.drawable.happy)
            EMOTION.SAD.toString() -> binding.moodIcon.setImageResource(R.drawable.sad)
            EMOTION.CRY.toString() -> binding.moodIcon.setImageResource(R.drawable.cry)
            EMOTION.NORMAL.toString() -> binding.moodIcon.setImageResource(R.drawable.normal)
        }
        if (roundedPulseValue != null) {
            when {
                roundedPulseValue!! < 40 -> binding.tvResultStatus.setText("very low")
                roundedPulseValue!! > 40 && roundedPulseValue!! < 60 -> binding.tvResultStatus.setText("low")
                roundedPulseValue!! > 60 && roundedPulseValue!! < 100 -> binding.tvResultStatus.setText("normal")
                roundedPulseValue!! > 100 && roundedPulseValue!! < 120 -> binding.tvResultStatus.setText("high")
                else -> binding.tvResultStatus.setText("very high")
            }
        }
        // Update the TextView with the pulse value
        binding.tvPulseValue.text = roundedPulseValue.toString()
        user = viewModel.getUserFromSharePref()
         age = user?.dateOfBirth?.let { viewModel.getAge(it) }.toString()
         gender = user!!.gender.toString()
        height = user?.height!!
        formattedHeight = String.format("%.2f", height)
         weight = user?.weight.toString()

        binding.premiumBtn.setOnClickListener {
            val intent = intent
            intent.setClass(this, SubscriptionActivity::class.java)
            startActivity(intent)
        }

        binding.txtAge.text = age
        binding.txtGender.text = gender
        binding.tvHeightValue.text = formattedHeight
        binding.tvWeightValue.text = weight


        val hbRecord = HBRecord(
            user = user,
            hr = roundedPulseValue,
            deviceId = "string",
            numCycles = 5,
//            emotion = when (emotion) {
//                EMOTION.JOY.toString() -> 1
//                EMOTION.HAPPY.toString() -> 2
//                EMOTION.SAD.toString() -> 3
//                EMOTION.CRY.toString() -> 4
//                EMOTION.NORMAL.toString() -> 5
//                else -> 0
//            },
            emotion = 1,
            activity = 1,
            hrv = 1

        )
        viewModel.createHBRecord(hbRecord)
        setupEvents()
        setupObservers()
    }

    private fun setupEvents() {
        binding.btnDianoses.setOnClickListener {
            binding.tvDiagnoseResult.text = "Diagnostic in progress..."
            val intRegex = Regex("[0-9]*")
            val bpm = roundedPulseValue
            val gender = gender
            val age = age
            val height= formattedHeight
            val weight = weight
            val diagnosisReq = DiagnosesReq(
                bpm,
                bpm,
                30,
                "7369607740777529345",
                "Now, my heart rate is ${bpm} bpm, my gender is ${gender}, my ${age} is 30 years old," +
                        " my height is ${height} m, my weight is ${weight} kg, " +
                        " I am currently resting, " +
                        " my mood is ${intent.getStringExtra("SELECTED_EMOTION")}, " +
                        ". Can you diagnose me?"
            )

            CoroutineScope(Dispatchers.IO).launch {
                val str = StringBuilder()
                viewModel.getDiagnoses(diagnosisReq).collect {
                    withContext(Dispatchers.Main) {
                        str.append(it.message?.content ?: "")
                        binding.tvDiagnoseResult.text = str.toString()
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

    private fun roundToNearestInt(value: Double): Int {
        return if (value - value.toInt() >= 0.5) {
            value.toInt() + 1
        } else {
            value.toInt()
        }
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
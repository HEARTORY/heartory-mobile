package com.heartsteel.heartory.ui.profile_onboarding

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.google.android.material.slider.Slider
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentSetDataBinding
import com.heartsteel.heartory.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class SetDataFragment : BaseFragment(R.layout.fragment_set_data) {

    private lateinit var binding: FragmentSetDataBinding
    private lateinit var profileViewModel: ProfileViewModel

    private val  myCalendar = Calendar.getInstance()
    private lateinit var calendarEditText : EditText
    private lateinit var heightTextValue: TextView
    private lateinit var weightTextValue: TextView
    private lateinit var heightSlider: Slider
    private lateinit var weightSlider: Slider
    private lateinit var maleBtn: ThemedButton
    private lateinit var femaleBtn: ThemedButton

    private lateinit var finishBtn: Button

    private var selectedDate: String? = null
    private var selectedGender :String? = null
    private var selectedHeight: Float? = null
    private var selectedWeight: Float? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetDataBinding.inflate(layoutInflater, container, false)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        calendarEditText = binding.edtDob
        heightTextValue = binding.txtHeightValue
        weightTextValue = binding.txtWeightValue
        heightSlider = binding.sliderHeight
        weightSlider = binding.sliderWeight
        maleBtn = binding.btnMale
        femaleBtn = binding.btnFemale
        finishBtn = binding.btnFinish

        calendarEditText.setOnClickListener {
            showDatePickerDialog()
        }

        weightSlider.addOnChangeListener { slider, value, fromUser ->
            weightTextValue.text = value.toString() + " kg"
            selectedWeight = value
        }

        heightSlider.addOnChangeListener { slider, value, fromUser ->
            heightTextValue.text = value.toString() + " m"
            selectedHeight = value
        }

        maleBtn.setOnClickListener {
            setGender("MALE")
        }

        femaleBtn.setOnClickListener {
            setGender("FEMALE")
        }

        finishBtn.setOnClickListener {
            finishFun()
        }
        return binding.root
    }

    private fun finishFun(){
        if(selectedDate != null && selectedGender != null && selectedHeight != null && selectedWeight != null){
            binding.btnFinish.isEnabled = false
            binding.btnFinish.isClickable = false
            binding.btnFinish.alpha = 0.5f
            lifecycleScope.launchWhenStarted {
                profileViewModel.setProfile(
                    selectedGender!!,
                    selectedDate!!,
                    selectedHeight!!.toDouble(),
                    selectedWeight!!.toDouble()
                )
                profileViewModel.userState.observe(viewLifecycleOwner) { userState ->
                    when (userState) {
                        is Resource.Loading -> {
                            showLoading2()
                        }
                        is Resource.Success -> {
                            hideLoading()
                             val mainIntent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(mainIntent)
                        }
                        is Resource.Error -> {
                            hideLoading()
                            showErrorMessage("Error setting profile")
                        }
                    }
                }
            }
        }else{
            showErrorMessage("Please fill all the fields")
        }
    }
    private fun setGender(gender: String){
        if(gender === "MALE"){
            selectedGender= "Male"
        }
        else
            selectedGender="Female"
    }
    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yyyy" // Change to your desired date format
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        selectedDate = sdf.format(myCalendar.time)
        calendarEditText.setText(selectedDate)
    }

}
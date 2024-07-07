package com.heartsteel.heartory.ui.profile.edit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.ActivityProfileEditBinding
import com.heartsteel.heartory.service.model.domain.User
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.getstream.avatarview.coil.loadImage

@AndroidEntryPoint
class ProfileEditActivity : AppCompatActivity() {

    private val binding: ActivityProfileEditBinding by lazy {
        ActivityProfileEditBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileEditViewModel by viewModels()

    val isError = MutableLiveData<MutableMap<String, Boolean>?>(
        mutableMapOf(
            "firstName" to false,
            "lastName" to false,
            "email" to false,
            "phone" to false,
            "dob" to false,
            "weight" to false,
            "height" to false
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEvent()
        setUpView()
        setipObserver()
    }

    private fun setipObserver() {
        viewModel.userLiveData.observe(this) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    Toasty.success(this, "Update successfully", Toasty.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    Toasty.error(this, it.message.toString(), Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupEvent() {
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnSave.setOnClickListener {
            if (isError.value?.containsValue(true) == false) {
                val user = User(
                    id = viewModel.user?.id,
                    role = viewModel.user?.role,
                    email = binding.etEmail1.text.toString(),
                    firstName = binding.etFirstname1.text.toString(),
                    lastName = binding.etLastname1.text.toString(),
                    phoneNumber = binding.etPhone1.text.toString(),
                    secondName = viewModel.user?.secondName,
                    avatar = viewModel.user?.avatar,
                    dateOfBirth = binding.etDob.text.toString(),
                    stanceId = viewModel.user?.stanceId,
                    weight = binding.etWeight.text.toString().toDouble(),
                    height = binding.etHeight.text.toString().toDouble(),
                    gender = if (binding.rgGender.checkedRadioButtonId == binding.rbMale.id) "Male" else "Female"
                )
                viewModel.updateUser(user)
            } else {
                Toasty.error(this, "Please correct all fields", Toasty.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpView() {
        val user = viewModel.userRepository.getUserFromSharePref()
        binding.etFirstname1.setText(user?.firstName)
        binding.etLastname1.setText(user?.lastName)
        binding.etEmail1.setText(user?.email)
        binding.rgGender.check(
            if (user?.gender?.lowercase().equals("male")) binding.rbMale.id else binding.rbFemale.id
        )
        binding.etDob.setText(user?.dateOfBirth)
        binding.etPhone1.setText(user?.phoneNumber)
        binding.etWeight.setText(user?.weight.toString())
        binding.etHeight.setText(user?.height.toString())
        if (viewModel.user?.avatar != null)
            binding.avUserAvatar1.loadImage(viewModel.user!!.avatar)
        else
            binding.avUserAvatar1.loadImage(R.drawable.heartory_app_logo)
        setupValidation()
    }

    private fun setupValidation() {
        binding.etFirstname1.addTextChangedListener {
            if (it.toString().trim().isEmpty()) {
                binding.tfFirstName.error = "First name is required"
                isError.value.let { map ->
                    map?.set("firstName", true)
                    isError.value = map
                }
            } else {
                binding.tfFirstName.error = null
                isError.value.let { map ->
                    map?.set("firstName", false)
                    isError.value = map
                }
            }
        }

        binding.etLastname1.addTextChangedListener {
            if (it.toString().trim().isEmpty()) {
                binding.tfLastName.error = "Last name is required"
                isError.value.let { map ->
                    map?.set("lastName", true)
                    isError.value = map
                }
            } else {
                binding.tfLastName.error = null
                isError.value.let { map ->
                    map?.set("lastName", false)
                    isError.value = map
                }
            }
        }

        binding.etEmail1.addTextChangedListener {
            val emailRegex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
            if (it.toString().trim().isEmpty()) {
                binding.tfEmail.error = "Email is required"
                isError.value.let { map ->
                    map?.set("email", true)
                    isError.value = map
                }
            } else if (!emailRegex.matches(it.toString())) {
                binding.tfEmail.error = "Invalid email format"
                isError.value.let { map ->
                    map?.set("email", true)
                    isError.value = map
                }
            } else {
                binding.tfEmail.error = null
                isError.value.let { map ->
                    map?.set("email", false)
                    isError.value = map
                }
            }
        }

        binding.etPhone1.addTextChangedListener {
            val phoneRegex = Regex("^[0-9]{10,12}\$")
            if (it.toString().trim().isEmpty()) {
                binding.tfPhone.error = "Phone number is required"
                isError.value.let { map ->
                    map?.set("phone", true)
                    isError.value = map
                }
            } else if (!phoneRegex.matches(it.toString())) {
                binding.tfPhone.error = "Invalid phone number"
                isError.value.let { map ->
                    map?.set("phone", true)
                    isError.value = map
                }
            } else {
                binding.tfPhone.error = null
                isError.value.let { map ->
                    map?.set("phone", false)
                    isError.value = map
                }
            }
            binding.etDob.addTextChangedListener {
                if (it.toString().trim().isEmpty()) {
                    binding.tfDob.error = "Date of birth is required"
                    isError.value.let { map ->
                        map?.set("dob", true)
                        isError.value = map
                    }
                } else {
                    binding.tfDob.error = null
                    isError.value.let { map ->
                        map?.set("dob", false)
                        isError.value = map
                    }
                }
            }
            binding.etWeight.addTextChangedListener {
                if (it.toString().trim().isEmpty()) {
                    binding.tfWeight.error = "Weight is required"
                    isError.value.let { map ->
                        map?.set("weight", true)
                        isError.value = map
                    }
                } else {
                    binding.tfWeight.error = null
                    isError.value.let { map ->
                        map?.set("weight", false)
                        isError.value = map
                    }
                }
            }
            binding.etHeight.addTextChangedListener {
                if (it.toString().trim().isEmpty()) {
                    binding.tfHeight.error = "Height is required"
                    isError.value.let { map ->
                        map?.set("height", true)
                        isError.value = map
                    }
                } else {
                    binding.tfHeight.error = null
                    isError.value.let { map ->
                        map?.set("height", false)
                        isError.value = map
                    }
                }
            }
        }
    }
}
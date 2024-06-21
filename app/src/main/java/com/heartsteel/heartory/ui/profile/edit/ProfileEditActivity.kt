package com.heartsteel.heartory.ui.profile.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.heartsteel.heartory.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {

    private val binding: ActivityProfileEditBinding by lazy {
        ActivityProfileEditBinding.inflate(layoutInflater)
    }

    val isError = MutableLiveData<MutableMap<String, Boolean>?>(
        mutableMapOf(
            "firstName" to true,
            "lastName" to true,
            "email" to true,
            "password" to true,
            "confirmPassword" to true
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        binding.
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
    }
}
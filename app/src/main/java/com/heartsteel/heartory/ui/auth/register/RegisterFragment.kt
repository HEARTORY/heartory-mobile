package com.heartsteel.heartory.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.constant.RoleEnum
import com.heartsteel.heartory.databinding.FragmentRegisterBinding
import com.heartsteel.heartory.service.model.request.RegisterReq
import com.heartsteel.heartory.ui.auth.AuthActivity
import com.heartsteel.heartory.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    companion object {
        fun newInstance() = RegisterFragment()
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

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        authViewModel = (requireActivity() as AuthActivity).viewModel
        setupView()
        setupEvent()
        setupObserver()
        setupValidation()
        return binding.root
    }

    private fun setupValidation() {
        binding.etFirstname.addTextChangedListener {
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

        binding.etLastname.addTextChangedListener {
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

        binding.etEmail.addTextChangedListener {
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

        binding.etEmail.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                authViewModel.isEmailExist(binding.etEmail.text.toString())
            }
        }

        binding.etPassword.addTextChangedListener {
            val passwordRegex =
                Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
            if (it.toString().trim().isEmpty()) {
                binding.tfPassword.error = "Password is required"
                isError.value.let { map ->
                    map?.set("password", true)
                    isError.value = map
                }
            } else if (it.toString().length < 8) {
                binding.tfPassword.error = "Password must be at least 8 characters"
                isError.value.let { map ->
                    map?.set("password", true)
                    isError.value = map
                }
            } else if (it.toString().length > 20) {
                binding.tfPassword.error = "Password must be at most 20 characters"
                isError.value.let { map ->
                    map?.set("password", true)
                    isError.value = map
                }
            } else if (!passwordRegex.matches(it.toString())) {
                binding.tfPassword.error =
                    "Password must contain at least one letter, one number, and one special character"
                isError.value.let { map ->
                    map?.set("password", true)
                    isError.value = map
                }
            } else {
                binding.tfPassword.error = null
                isError.value.let { map ->
                    map?.set("password", false)
                    isError.value = map
                }
            }
        }
        binding.etPasswordConfirm.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.tfPasswordConfirm.error = "Confirm password is required"
                isError.value.let { map ->
                    map?.set("confirmPassword", true)
                    isError.value = map
                }
            } else if (it.toString() != binding.etPassword.text.toString()) {
                binding.tfPasswordConfirm.error = "Password not match"
                isError.value.let { map ->
                    map?.set("confirmPassword", true)
                    isError.value = map
                }
            } else {
                binding.tfPasswordConfirm.error = null
                isError.value.let { map ->
                    map?.set("confirmPassword", false)
                    isError.value = map
                }
            }
        }
    }

    private fun setupObserver() {
        isError.observe(viewLifecycleOwner) {
            if (it?.containsValue(true) == true) {
                binding.btnSignUp.isEnabled = false
                binding.btnSignUp.setBackgroundResource(R.drawable.bg_button_disabled)
            } else {
                binding.btnSignUp.isEnabled = true
                binding.btnSignUp.setBackgroundResource(R.drawable.bg_gradient_blue_lightblue)
            }
        }
    }

    private fun setupView() {

    }

    private fun setupEvent() {
        binding.ivLoginWithGoogle.setOnClickListener {
            (activity as AuthActivity).loginWithGoogle()
        }
        binding.tvLogin.setOnClickListener {
            (activity as AuthActivity).navigateToLogin()
        }
        binding.btnSignUp.setOnClickListener {
            val role = RoleEnum.USER.value
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etPasswordConfirm.text.toString()
            var registerReq = RegisterReq(
                firstName,
                lastName,
                email,
                password,
                role,
            );
            if (password == confirmPassword) {
                lifecycleScope.launchWhenStarted {
                    authViewModel.register(registerReq)
                }
            } else {
                Toast.makeText(requireContext(), "Password not match", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
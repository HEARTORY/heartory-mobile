package com.heartsteel.heartory.ui.auth.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentLoginBinding
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.ui.auth.AuthActivity
import com.heartsteel.heartory.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import okhttp3.internal.notify

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {
    companion object {
        fun newInstance() = LoginFragment()
    }

    val isError = MutableLiveData<MutableMap<String, Boolean>?>(
        mutableMapOf(
            "email" to true,
            "password" to true
        )
    )

    lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        authViewModel = (requireActivity() as AuthActivity).viewModel
        setupView()
        setupEvent()
        setupObserver()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupValidation()
    }

    private fun setupObserver() {
        isError.observe(viewLifecycleOwner) {
            if (it?.containsValue(true) == true) {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.setBackgroundResource(R.drawable.bg_button_disabled)
            } else {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.setBackgroundResource(R.drawable.bg_gradient_blue_lightblue)
            }
        }
    }

    private fun setupValidation() {
        binding.etEmail.addTextChangedListener {
            if (it.toString().trim().isEmpty()) {
                binding.tfEmail.error = "Email is required"
                isError.value.let { map ->
                    map?.set("email", true)
                    isError.value = map
                }

            } else if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
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

        binding.etPassword.addTextChangedListener {
            if (it.toString().trim().isEmpty()) {
                binding.tfPassword.error = "Password is required"
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
    }

    private fun setupView() {


    }

    private fun setupEvent() {
        authViewModel.user

        binding.tvRegister.setOnClickListener {
            (activity as AuthActivity).navigateToRegister()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
             lifecycleScope.launchWhenStarted{
                var loginReq = LoginReq(email, password)
                authViewModel.login(loginReq)
            }
        }

        binding.ivLoginWithGoogle.setOnClickListener {
            (activity as AuthActivity).loginWithGoogle()
        }

        binding.tvForgotPassword.setOnClickListener {
            if (isError.value?.get("email") == true) {
                Toasty.error(requireContext(), "Please provide a valid email", Toasty.LENGTH_SHORT)
                    .show()
            } else {
                val email = binding.etEmail.text.toString()
                authViewModel.forgotPassword(email)
            }

        }
    }


}
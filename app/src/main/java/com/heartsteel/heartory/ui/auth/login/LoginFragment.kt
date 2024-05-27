package com.heartsteel.heartory.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.databinding.FragmentLoginBinding
import com.heartsteel.heartory.ui.auth.AuthActivity
import com.heartsteel.heartory.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        authViewModel =  (requireActivity() as AuthActivity).viewModel
        setupView()
        setupEvent()

        return binding.root
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
            lifecycleScope.launchWhenStarted {
                var loginReq = LoginReq(email, password)
                authViewModel.login(loginReq)
            }
        }

        binding.ivLoginWithGoogle.setOnClickListener{
            (activity as AuthActivity).loginWithGoogle()
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            authViewModel.forgotPassword(email)
        }
    }



}
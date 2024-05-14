package com.heartsteel.heartory.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.databinding.FragmentLoginBinding
import com.heartsteel.heartory.ui.MainActivity
import com.heartsteel.heartory.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    lateinit var binding: FragmentLoginBinding
    private val _viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
//        val authRepository = AuthRepository()
//        val viewModelFactory = LoginViewModelFactory(requireActivity().application, authRepository)
//        _viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupView()
        setupEvent()
        setupObserver()
        return binding.root
    }

    private fun setupView() {
        (requireActivity() as AuthActivity).loginWithGoogle {

        }

    }

    private fun setupEvent() {

        binding.tvRegister.setOnClickListener {
            (activity as AuthActivity).navigateToRegister()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            lifecycleScope.launchWhenStarted {
                var loginReq = LoginReq(email, password)
                _viewModel.login(loginReq)
            }
        }
    }

    private fun setupObserver() {
        _viewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading("Logging in...", "Please wait...")
                }

                is Resource.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Log in success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        requireContext(),
                        "Login failed" + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}
package com.heartsteel.heartory.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.repository.AuthRepository
import com.heartsteel.heartory.databinding.FragmentLoginBinding
import com.heartsteel.heartory.ui.MainActivity
import com.heartsteel.heartory.ui.auth.AuthActivity

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    lateinit var binding: FragmentLoginBinding
    private lateinit var _viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val authRepository = AuthRepository()
        val viewModelFactory = LoginViewModelFactory(requireActivity().application, authRepository)
        _viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        setupView()
        setupEvent()
        return binding.root
    }

    private fun setupView() {
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

    private fun setupEvent() {

    }


}
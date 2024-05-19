package com.heartsteel.heartory.ui.auth.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.constant.RoleEnum
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.repository.AuthRepository
import com.heartsteel.heartory.databinding.FragmentRegisterBinding
import com.heartsteel.heartory.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    companion object{
        fun newInstance() = RegisterFragment()
    }

    private lateinit var _binding: FragmentRegisterBinding
    private val _viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
//        val authRepository = AuthRepository()
//        val viewModelFactory = RegisterViewModelFactory(requireActivity().application, authRepository)
//        _viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {

    }
    private fun setupEvent() {
        _binding.tvLogin.setOnClickListener{
            (activity as AuthActivity).navigateToLogin()
        }
        _binding.btnSignUp.setOnClickListener {
            val role = RoleEnum.ADMIN.value
            val firstName = _binding.etFirstname.text.toString()
            val lastName = _binding.etLastname.text.toString()
            val email = _binding.etEmail.text.toString()
            val password = _binding.etPassword.text.toString()
            val confirmPassword = _binding.etPasswordConfirm.text.toString()
            var registerReq = RegisterReq(firstName, lastName, email, password, role);
            if (password == confirmPassword) {
                lifecycleScope.launchWhenStarted {
                    _viewModel.register(registerReq)
                }
            } else {
                Toast.makeText(requireContext(), "Password not match", Toast.LENGTH_SHORT).show()
            }
        }
        _viewModel.registerState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading("Registering...", "Please wait...")
                }
                is Resource.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()
                    (activity as AuthActivity).navigateToLogin()
                }
                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
package com.heartsteel.heartory.ui.auth.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    companion object{
        fun newInstance() = LoginFragment()
    }

    private  lateinit var _binding: FragmentLoginBinding
    private val _viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {

    }

    private fun setupEvent() {

    }


}
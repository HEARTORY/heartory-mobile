package com.heartsteel.heartory.ui.auth.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentRegisterBinding

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
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {

    }
    private fun setupEvent() {

    }


}
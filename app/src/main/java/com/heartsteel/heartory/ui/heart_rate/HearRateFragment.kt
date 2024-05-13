package com.heartsteel.heartory.ui.heart_rate

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentHearRateBinding

class HearRateFragment : BaseFragment(R.layout.fragment_hear_rate) {
    companion object {
        fun newInstance() = HearRateFragment()
    }

    private lateinit var _binding: FragmentHearRateBinding
    private val _viewModel: HearRateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHearRateBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupEvent() {

    }

    private fun setupView() {

    }
}
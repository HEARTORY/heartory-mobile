package com.heartsteel.heartory.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentHomeBinding
import io.getstream.avatarview.coil.loadImage

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var _binding: FragmentHomeBinding
    private val _viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {

        _binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)
    }

    private fun setupEvent() {
        _binding.cvChat.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_chatFragment)
        }
        _binding.cvAnalysis.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_analysisFragment)
        }
        _binding.cvExercise.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_exerciseFragment)
        }
        _binding.cvArticle.setOnClickListener {
            navigateToPage(R.id.action_homeFragment_to_articleFragment)
        }

    }


}
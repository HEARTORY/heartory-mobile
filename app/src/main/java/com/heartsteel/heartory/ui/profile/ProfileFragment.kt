package com.heartsteel.heartory.ui.profile

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentProfileBinding
import com.heartsteel.heartory.ui.auth.AuthActivity
import io.getstream.avatarview.coil.loadImage

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var _binding: FragmentProfileBinding
    private val _viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {
        _binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)

    }

    private fun setupEvent() {
        _binding.llLogout.setOnClickListener {
            Intent(requireContext(), AuthActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

    private fun logout() {

    }
}
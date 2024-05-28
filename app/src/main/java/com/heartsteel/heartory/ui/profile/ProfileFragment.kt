package com.heartsteel.heartory.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentProfileBinding
import com.heartsteel.heartory.ui.auth.AuthActivity
import com.heartsteel.heartory.ui.profile.edit.ProfileEditActivity
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.avatarview.coil.loadImage

@AndroidEntryPoint
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
        setupObserver()
        return _binding.root
    }

    private fun setupView() {
        if (_viewModel.user?.avatar != null)
            _binding.avUserAvatar.loadImage(_viewModel.user!!.avatar)
        else
            _binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)

        _binding.tvUsername.text = _viewModel.user?.email ?: "Email"
        _binding.tvName.text = _viewModel.user?.firstName ?: "Username"

    }

    private fun setupEvent() {
        _binding.llLogout.setOnClickListener {
            _viewModel.logout()
            Intent(requireContext(), AuthActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
        _binding.llEditProfile.setOnClickListener {
            _viewModel.fetchUser()
        }
    }

    private fun setupObserver() {
        _viewModel.userState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    it.data?.let {
                        Toast.makeText(requireContext(), "${it}", Toast.LENGTH_SHORT).show()
                        Intent(requireContext(), ProfileEditActivity::class.java).also {

                            startActivity(it)
                        }
                    }
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
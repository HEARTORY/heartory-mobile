package com.heartsteel.heartory.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentHomeBinding
import io.getstream.avatarview.coil.loadImage

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var _binding: FragmentHomeBinding
    private val _viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel

    }

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
    }


}
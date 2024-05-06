package com.heartsteel.heartory.ui.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentChatBinding

class ChatFragment : BaseFragment(R.layout.fragment_chat) {

    private lateinit var _binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return _binding.root
    }

    private fun setupView() {

    }

    private fun setupEvent() {

    }


}
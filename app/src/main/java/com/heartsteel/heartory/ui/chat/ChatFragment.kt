package com.heartsteel.heartory.ui.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
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
        val spannable = SpannableString("Chat with Mia")
        _binding.tvChatTitle.text = spannable
    }

    private fun setupEvent() {

    }


}
package com.heartsteel.heartory.ui.chat.welcome

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentChatBinding
import com.heartsteel.heartory.ui.chat.inside.ChatInsideActivity

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
        _binding.btnChatNow.setOnClickListener {
            startActivity(Intent(this.context, ChatInsideActivity::class.java))
        }
    }


}
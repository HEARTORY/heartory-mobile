package com.heartsteel.heartory.ui.chat.inside

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.Message
import com.heartsteel.heartory.databinding.ActivityChatInsideBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatInsideActivity : BaseActivity() {
    private lateinit var _binding: ActivityChatInsideBinding
    private val viewModel: ChatInsideViewModel by viewModels()

    private lateinit var _chatInsideAdapter: ChatInsideAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityChatInsideBinding.inflate(layoutInflater)
        setContentView(_binding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        setupView()
        setupEvent()
        setupObserver()
    }

    private fun setupView() {
        // _chatInsideAdapter
        _chatInsideAdapter = ChatInsideAdapter()
        _binding.rvChat.apply {
            adapter = _chatInsideAdapter
            layoutManager = LinearLayoutManager(this@ChatInsideActivity)
        }
        //mock
        viewModel.messages.value = Resource.Success(viewModel.mockData)

    }

    private fun setupEvent() {
        _binding.etInput.setOnFocusChangeListener{
            _, hasFocus ->
            if(hasFocus){
                _binding.rvChat.scrollToPosition(_chatInsideAdapter.itemCount - 1)
            }
        }

        _binding.btnVector.setOnClickListener {
            viewModel.messages.value?.data?.add(
                Message(
                    id = "id",
                    content = _binding.etInput.text.toString(),
                    userId = "userId",
                    isByUser = true,
                    timeStamp = System.currentTimeMillis(),
                    sent = true,
                    seen = true
                )
            )
            viewModel.messages.value = Resource.Success(viewModel.messages.value!!.data!!)
            
            _binding.etInput.text?.clear()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding.btnVector.windowToken, 0)
        }

    }

    private fun setupObserver() {
        viewModel.messages.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        _chatInsideAdapter.submitList(it)
                    }
                }

                is Resource.Error -> {
                    // handle error
                }

                is Resource.Loading -> {
                    // handle loading
                }
            }
        }
    }
}
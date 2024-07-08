package com.heartsteel.heartory.ui.chat.inside

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.ActivityChatInsideBinding
import com.heartsteel.heartory.service.model.domain.Message
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlin.math.roundToInt

@AndroidEntryPoint
class ChatInsideActivity : BaseActivity() {
    private lateinit var _binding: ActivityChatInsideBinding
    private val viewModel: ChatInsideViewModel by viewModels()

    private lateinit var _chatInsideAdapter: ChatInsideAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityChatInsideBinding.inflate(layoutInflater)
        setContentView(
            _binding.root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

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

        _chatInsideAdapter.setItemOrderBy { list ->
            list.sortedBy { it.id }
            Log.d("ChatInsideActivity", "list: $list")
        }


        //getMessages
        viewModel.getMessages()

        if (viewModel.messages.value?.data?.isNotEmpty() == true) {
            _binding.rvChat.scrollToPosition(viewModel.messages.value!!.data!!.size - 1)
        }


        // Scroll to bottom when keyboard is shown or hidden
        val activityRootView = findViewById<View>(R.id.rvChat)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val heightDiff = activityRootView.rootView.height - activityRootView.height
                if (heightDiff > dpToPx(100) || heightDiff < -dpToPx(100)) {
                    if (viewModel.messages.value?.data?.size != null && viewModel.messages.value?.data?.size!! > 0)
                        _binding.rvChat.smoothScrollToPosition(viewModel.messages.value!!.data!!.size - 1)
                }
            }

            private fun dpToPx(dp: Int): Int {
                val density = resources.displayMetrics.density
                return (dp * density).roundToInt()
            }
        })
    }

    private fun setupEvent() {
        _binding.btnVector.setOnClickListener {

            if (_binding.etInput.text.toString().isEmpty()) return@setOnClickListener

            val messages = viewModel.messages.value?.data
            messages?.let {
                messages.add(Message(content = _binding.etInput.text.toString(), role = "user"))
                messages.add(Message(content = "", role = _chatInsideAdapter.LEFT_LOADING_ROLE))
                viewModel.messages.postValue(Resource.Success(messages))
            }

            viewModel.sendMessage(_binding.etInput.text.toString())

            _binding.etInput.text?.clear()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding.btnVector.windowToken, 0)
        }

        _binding.ivBack.setOnClickListener {
            finish()
        }
    }

    // Hide keyboard when touch outside of EditText
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (_binding.etInput.isFocused) {
                val outRect = Rect()
                _binding.etInput.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    _binding.etInput.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(_binding.etInput.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setupObserver() {
        viewModel.messages.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        _chatInsideAdapter.submitList(it)
                        _binding.tvEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    }
                }

                is Resource.Error -> {
                    Log.e("ChatInsideActivity", "Error: ${resource.message}")
                    Toasty.error(this, "Error: ${resource.message}", Toasty.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                }
            }
        }

        viewModel.sendMessageState.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    viewModel.getMessages()
                }

                is Resource.Error -> {
                    Log.e("ChatInsideActivity", "Error: ${resource.message}")
                    Toasty.error(this, "Error: ${resource.message}", Toasty.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                }
            }
        }
    }
}
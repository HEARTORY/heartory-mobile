package com.heartsteel.heartory.ui.exercise

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayDetailExerciseListBinding

class ExerciseTodayDetailFragment : Fragment() {

    private var _binding: FragmentExerciseTodayDetailExerciseListBinding? = null
    private val binding get() = _binding!!
    private var isFullScreen = false
    private lateinit var videoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseTodayDetailExerciseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Using standard VideoView from the Android framework
        val videoView: VideoView = binding.videoView

        // Set the path for your video file from a cloud database (e.g., Firebase Storage, AWS S3, etc.)
        videoUri = Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.video)
        videoView.setVideoURI(videoUri)

        // Setup a MediaController
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Start video when it is ready
        videoView.setOnPreparedListener { mp ->
            mp.start()
        }
        videoView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                toggleFullscreen()
                true
            } else {
                false
            }
        }

        binding.videoViewFullScreen.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                toggleFullscreen()
                true
            } else {
                false
            }
        }

        setupRecyclerView()
        backClickListener()
    }

    private fun setupRecyclerView() {
        val exercises = listOf(
            Exercise("Exercise 1", "02.30 Minutes"),
            Exercise("Exercise 2", "02.00 Minutes"),
            Exercise("Exercise 3", "03.20 Minutes")
        )

        val exerciseAdapter = ExerciseTodayDetailAdapter(exercises)
        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exerciseAdapter
        }
    }

    private fun toggleFullscreen() {
        isFullScreen = !isFullScreen
        if (isFullScreen) {
            binding.regularLayout.visibility = View.GONE
            binding.fullscreenLayout.visibility = View.VISIBLE
            binding.videoViewFullScreen.setVideoURI(videoUri)
            binding.videoViewFullScreen.start()
            hideSystemUI()
        } else {
            binding.regularLayout.visibility = View.VISIBLE
            binding.fullscreenLayout.visibility = View.GONE
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.start()
            showSystemUI()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.let {
                it.show(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTodayDetailExerciseListFragment_to_exerciseTodayActivityListFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


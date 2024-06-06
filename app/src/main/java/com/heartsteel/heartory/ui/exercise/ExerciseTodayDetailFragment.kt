package com.heartsteel.heartory.ui.exercise

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
//        val videoView: VideoView = binding.videoView

//        // Set the path for your video file from a cloud database (e.g., Firebase Storage, AWS S3, etc.)
//        videoUri = Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.video)
//        videoView.setVideoURI(videoUri)
//
//        // Setup a MediaController
//        val mediaController = MediaController(requireContext())
//        mediaController.setAnchorView(videoView)
//        videoView.setMediaController(mediaController)
//
//        // Start video when it is ready
//        videoView.setOnPreparedListener { mp ->
//            mp.start()
//        }
//        videoView.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                toggleFullscreen()
//                true
//            } else {
//                false
//            }
//        }
//
//        binding.videoViewFullScreen.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                toggleFullscreen()
//                true
//            } else {
//                false
//            }
//        }
        // Using WebView to display the video
        val webView: WebView = binding.videoView

        // Get the video URL passed from the previous fragment
        val videoUrl = arguments?.getString("videoUrl") ?: ""
        setupWebView(webView, videoUrl)


        setupRecyclerView()
        backClickListener()
    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise(name = "Knee push up", time = "Today, 03:00pm", imageUrl = "https://media1.popsugar-assets.com/files/thumbor/vuQmhXf-dfGzBK2liB8_4f6kuF8/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2014/04/04/724/n/1922729/b95be44e0421cbd2_Plank-Knee-In/i/Knee-Up-Plank.jpg", videoUrl = "https://www.youtube.com/embed/WcHtt6zT3Go"),
            Exercise(name = "Push up", time = "Today, 03:00pm", imageUrl = "https://www.realsimple.com/thmb/rEmEAm4vfx67IRbFgoVA0RzhTgI=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/health-benefits-of-pushups-GettyImages-498315681-7008d40842444270868c88b516496884.jpg", videoUrl = "https://www.youtube.com/embed/IODxDxX7oi4"),
            Exercise(name = "Sit up", time = "Today, 03:00pm", imageUrl = "https://hips.hearstapps.com/hmg-prod/images/2021-runnersworld-weekendworkouts-ep41-situps-jc-v03-index-1633617537.jpg?crop=0.9893230083304001xw:1xh;center,top&resize=1200:*", videoUrl = "https://www.youtube.com/embed/pCX65Mtc_Kk")
        )

        val adapter = ExerciseTodayDetailAdapter(exerciseList) { exercise ->
            exercise.videoUrl?.let {
                val action = ExerciseTodayDetailFragmentDirections.actionToSameFragment(it)
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

//    private fun toggleFullscreen() {
//        isFullScreen = !isFullScreen
//        if (isFullScreen) {
//            binding.regularLayout.visibility = View.GONE
//            binding.fullscreenLayout.visibility = View.VISIBLE
//            binding.videoViewFullScreen.setVideoURI(videoUri)
//            binding.videoViewFullScreen.start()
//            hideSystemUI()
//        } else {
//            binding.regularLayout.visibility = View.VISIBLE
//            binding.fullscreenLayout.visibility = View.GONE
//            binding.videoView.setVideoURI(videoUri)
//            binding.videoView.start()
//            showSystemUI()
//        }
//    }
//
//    private fun hideSystemUI() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.insetsController?.let {
//                it.hide(WindowInsets.Type.systemBars())
//                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            @Suppress("DEPRECATION")
//            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
//        }
//    }
//
//    private fun showSystemUI() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.insetsController?.let {
//                it.show(WindowInsets.Type.systemBars())
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            @Suppress("DEPRECATION")
//            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        }
//    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTodayDetailExerciseListFragment_to_exerciseTodayActivityListFragment)

        }
    }

    private fun setupWebView(webView: WebView, url: String) {
        webView.webViewClient = WebViewClient()
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.mediaPlaybackRequiresUserGesture = false

        webView.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


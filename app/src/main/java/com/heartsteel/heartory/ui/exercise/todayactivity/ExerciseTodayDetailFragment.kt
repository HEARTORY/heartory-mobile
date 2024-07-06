package com.heartsteel.heartory.ui.exercise.todayactivity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayDetailExerciseListBinding
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.model.domain.Lesson
import com.heartsteel.heartory.service.repository.ExerciseRepository
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import com.heartsteel.heartory.ui.exercise.ExerciseViewModel
import com.heartsteel.heartory.ui.exercise.ViewModelFactory

class ExerciseTodayDetailFragment : Fragment() {

    private var _binding: FragmentExerciseTodayDetailExerciseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExerciseViewModel
    private var currentVideoUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseTodayDetailExerciseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        val appSharePreference = AppSharePreference(requireContext())
        val tokenManager = JwtRepository(appSharePreference)
        val publicRetrofit = PublicRetrofit()
        val jwtTokenInterceptor = JwtTokenInterceptor(tokenManager, publicRetrofit)
        val privateRetrofit = PrivateRetrofit(jwtTokenInterceptor)
        val repository = ExerciseRepository(privateRetrofit)

        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(repository)).get(ExerciseViewModel::class.java)
        // Get the initial video URL passed from the previous fragment
        currentVideoUrl = arguments?.getString("videoUrl") ?: "https://www.youtube.com/embed/-p0PA9Zt8zk"
        Log.d("currentVideoUrl", "Video URL: $currentVideoUrl")
        setupWebView(currentVideoUrl!!)
        setupRecyclerView()
        backClickListener()
        setupObservers()


        binding.progressBar.visibility = View.VISIBLE
        binding.regularLayout.visibility = View.GONE
    }

    private fun setupObservers() {
        viewModel.myLessons.observe(viewLifecycleOwner) { lessons ->
            // Update the RecyclerView
            updateRecyclerView(lessons)

            // Update the initial video details
            updateVideoDetails(lessons.find { it.videokey == currentVideoUrl })
            binding.progressBar.visibility = View.GONE
            binding.regularLayout.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        val adapter = ExerciseTodayDetailAdapter(emptyList()) { lesson ->
            lesson.videokey?.let {
                currentVideoUrl = it
                updateVideoDetails(lesson)
                updateRecyclerView(viewModel.myLessons.value ?: emptyList())
            }
        }

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun updateRecyclerView(lessons: List<Lesson>) {
        val filteredLessons = lessons.filter { it.videokey != currentVideoUrl }
        (binding.recyclerViewExercises.adapter as ExerciseTodayDetailAdapter).updateLessons(filteredLessons)
    }

    private fun updateVideoDetails(lesson: Lesson?) {
        val videoUrl = lesson?.videokey ?: "https://www.youtube.com/embed/-p0PA9Zt8zk"
        val videoTitle = lesson?.lessonName ?: "Default Title"
        val videoDescription = lesson?.updatedAt ?: "Default Description"
        Log.d("ExerciseTodayDetailFragment", "Video URL: $videoUrl")

        setupWebView(videoUrl)
        binding.textViewVideoTitle.text = videoTitle
        binding.textViewVideoDescription.text = videoDescription
    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTodayDetailExerciseListFragment_to_exerciseTodayActivityListFragment)
        }
    }

    private fun setupWebView(url: String) {
        binding.videoView.apply {
            webViewClient = WebViewClient()
            val webSettings: WebSettings = settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.mediaPlaybackRequiresUserGesture = false

            loadUrl(url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


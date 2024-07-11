package com.heartsteel.heartory.ui.exercise.recommendation


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.ToastUtil
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseRecommendationVideoListBinding
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.repository.ExerciseRepository
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import com.heartsteel.heartory.ui.exercise.ExerciseViewModel
import com.heartsteel.heartory.ui.exercise.ViewModelFactory
import com.heartsteel.heartory.ui.exercise.recommendation.ExerciseRecommendationActivityVideoFragmentDirections


class ExerciseRecommendationActivityVideoFragment : Fragment() {

    private var _binding: FragmentExerciseRecommendationVideoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExerciseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseRecommendationVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseId = arguments?.getInt("exerciseId") ?: return
        val lessonId = arguments?.getInt("lessonId") ?: return
        val enrollmentMessage = arguments?.getString("enrollmentMessage")

        // Initialize the ViewModel
        val appSharePreference = AppSharePreference(requireContext())
        val tokenManager = JwtRepository(appSharePreference)
        val publicRetrofit = PublicRetrofit()
        val jwtTokenInterceptor = JwtTokenInterceptor(tokenManager, publicRetrofit)
        val privateRetrofit = PrivateRetrofit(jwtTokenInterceptor)
        val repository = ExerciseRepository(privateRetrofit)

        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(repository)).get(ExerciseViewModel::class.java)

        // Show progress bar and hide the content while loading
        binding.progressBar.visibility = View.VISIBLE
        binding.regularLayout.visibility = View.GONE

        viewModel.allExercises.observe(viewLifecycleOwner, { exercises ->
            val exercise = exercises?.find { it.id == exerciseId }
            val lesson = exercise?.lessons?.find { it.id == lessonId }
            val videoUrl = lesson?.videokey ?: "https://www.youtube.com/embed/-p0PA9Zt8zk"

            setupWebView(binding.videoView, videoUrl)

            binding.textViewVideoTitle.text = lesson?.lessonName ?: "Default Title"
            binding.textViewVideoDescription.text = lesson?.updatedAt ?: "Default Description"

            // Hide progress bar and show the content once data is loaded
            binding.progressBar.visibility = View.GONE
            binding.regularLayout.visibility = View.VISIBLE


        })

        setupObservers(exerciseId, lessonId)
        setupBackClickListener(exerciseId)
    }

    private fun setupWebView(webView: WebView, url: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.e("WebViewError", "Error loading video URL: ${error?.description}")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "Page finished loading: $url")
            }
        }

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.mediaPlaybackRequiresUserGesture = false

        Log.d("setupWebView", "Loading video URL: $url")
        webView.loadUrl(url)
    }

    private fun setupObservers(exerciseId: Int, lessonId: Int) {
        viewModel.allExercises.observe(viewLifecycleOwner, { exercises ->
            exercises?.forEach { Log.d("ExerciseActivityVideoFragment", it.toString()) }

            exercises?.let {
                setupRecyclerView(exerciseId, lessonId, it)
            } ?: setupRecyclerView(exerciseId, lessonId, emptyList())
        })
    }

    private fun setupRecyclerView(exerciseId: Int, lessonId: Int, exercises: List<Exercise>) {
        val lessons = exercises.find { it.id == exerciseId }?.lessons?.filter { it.id != lessonId } ?: emptyList()

        val adapter = ExerciseRecommendationActivityVideoAdapter(lessons) { lesson ->
            val videoUrl = lesson.videokey ?: "https://www.youtube.com/embed/-p0PA9Zt8zk"
            val action = ExerciseRecommendationActivityVideoFragmentDirections
                .actionExerciseActivityRecommendationVideoFragmentSelf(lesson.id, videoUrl, exerciseId, "")
            findNavController().navigate(action)
        }

        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun setupBackClickListener(exerciseId: Int) {
        binding.imageViewBackArrow.setOnClickListener {
            val action = ExerciseRecommendationActivityVideoFragmentDirections
                .actionExerciseActivityRecommendationVideoFragmentToExerciseRecommendationActivityListFragment(exerciseId, "")
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
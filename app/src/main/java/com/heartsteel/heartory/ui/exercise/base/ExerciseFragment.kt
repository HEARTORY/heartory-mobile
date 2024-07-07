package com.heartsteel.heartory.ui.exercise.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.ToastUtil
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseBinding
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.repository.ExerciseRepository
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import com.heartsteel.heartory.ui.exercise.ExerciseViewModel
import com.heartsteel.heartory.ui.exercise.ViewModelFactory


class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var adapter: ExerciseCategoryAdapter
    private var allExercisesLoaded = false
    private var recommendationsLoaded = false
    private var currentRecommendation: Exercise? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create instances of necessary dependencies
        val appSharePreference = AppSharePreference(requireContext())
        val tokenManager = JwtRepository(appSharePreference)
        val publicRetrofit = PublicRetrofit()
        val jwtTokenInterceptor = JwtTokenInterceptor(tokenManager, publicRetrofit)
        val privateRetrofit = PrivateRetrofit(jwtTokenInterceptor)
        val repository = ExerciseRepository(privateRetrofit)

        // Initialize the ViewModel scoped to the activity
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(repository)).get(ExerciseViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        seeAllClickListener()
        todayActivityClickListener()
        recommendationClassClickListener()
        recommendationVideoClickListener()
        // Fetch exercises when the fragment is created
        showLoading()
        viewModel.fetchAllExercises()
        viewModel.fetchRecommendations()
        viewModel.fetchMyExercises()
    }

    private fun setupRecyclerView() {
        adapter = ExerciseCategoryAdapter(emptyList()) { exercise ->
            exercise.id?.let { exerciseId ->
                val action = ExerciseFragmentDirections.actionExerciseFragmentToExerciseActivityListFragment(exerciseId, "")
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ExerciseFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.allExercises.observe(viewLifecycleOwner, { exercises ->
            exercises?.forEach { Log.d("ExerciseFragment", it.toString()) }
            adapter.updateExercises(exercises ?: emptyList())
            allExercisesLoaded = true
            checkDataLoaded()
        })

        viewModel.recommendations.observe(viewLifecycleOwner, { exercises ->
            exercises?.firstOrNull()?.let {
                currentRecommendation = it
                updateRecommendationCard(it)
            }
            recommendationsLoaded = true
            checkDataLoaded()
        })
    }

    private fun updateRecommendationCard(exercise: Exercise) {
        binding.recommendationClass.apply {
            findViewById<TextView>(R.id.tvClassName).text = exercise.title
            findViewById<TextView>(R.id.tvInstructorName).text = exercise.type
            findViewById<ImageView>(R.id.ivClassLogo).apply {
                Glide.with(this)
                    .load(exercise.thumbUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(this)
            }
        }
    }

    private fun seeAllClickListener() {
        binding.tvSeeAllCategories.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseActivityListFragment)
        }
    }

    private fun todayActivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseTodayActivityListFragment)
        }
    }

    private fun recommendationClassClickListener() {
        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseRecommendationActivityListFragment)
        }
    }

    private fun recommendationVideoClickListener() {
        binding.recommendationClass.setOnClickListener {
            currentRecommendation?.let { recommendation ->
                // Get the first lesson from the lessons list
                val firstLesson = recommendation.lessons?.firstOrNull()
                if (firstLesson != null) {
                    val action = ExerciseFragmentDirections.actionExerciseFragmentToExerciseActivityRecommendationVideoFragment(
                        exerciseId = recommendation.id,
                        lessonId = firstLesson.id,
                        videoUrl = firstLesson.videokey ?: "",
                        enrollmentMessage = ""
                    )
                    findNavController().navigate(action)
                } else {
                    // Handle case where there are no lessons
                    Toast.makeText(context, "No lessons available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.vHomeHeader.visibility = View.GONE
        binding.recyclerViewCategories.visibility = View.GONE
        binding.recommendationClass.visibility = View.GONE
        binding.tvCategories.visibility = View.GONE
        binding.tvRecommendationClass.visibility = View.GONE
        binding.tvSeeAllCategories.visibility = View.GONE
        binding.tvSeeAll.visibility = View.GONE
        binding.todayActivity.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.vHomeHeader.visibility = View.VISIBLE
        binding.recyclerViewCategories.visibility = View.VISIBLE
        binding.recommendationClass.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvRecommendationClass.visibility = View.VISIBLE
        binding.tvSeeAllCategories.visibility = View.VISIBLE
        binding.tvSeeAll.visibility = View.VISIBLE
        binding.todayActivity.visibility = View.VISIBLE
    }

    private fun checkDataLoaded() {
        if (allExercisesLoaded && recommendationsLoaded) {
            hideLoading()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
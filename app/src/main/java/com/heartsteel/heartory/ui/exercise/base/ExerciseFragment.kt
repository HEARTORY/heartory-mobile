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
    private lateinit var categoryAdapter: ExerciseCategoryAdapter
    private lateinit var enrollAdapter: ExerciseEnrollAdapter
    private lateinit var recommendationAdapter: ExerciseRecommendationAdapter

    private var allExercisesLoaded = false
    private var recommendationsLoaded = false
    private var myExercisesLoaded = false

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
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        // Show loading before fetching data
        showLoading()

        // Fetch exercises every time the fragment is resumed
        viewModel.fetchAllExercises()
        viewModel.fetchRecommendations()
        viewModel.fetchMyExercises()
    }

    private fun setupRecyclerView() {
        categoryAdapter = ExerciseCategoryAdapter(emptyList()) { exercise ->
            exercise.id?.let { exerciseId ->
                val action = ExerciseFragmentDirections.actionExerciseFragmentToExerciseActivityListFragment(exerciseId, "")
                findNavController().navigate(action)
            }
        }
        enrollAdapter = ExerciseEnrollAdapter(emptyList()) { exerciseMyResponseDTO ->
            val exerciseId = exerciseMyResponseDTO.exercise.id
            val action = ExerciseFragmentDirections.actionExerciseFragmentToExerciseTodayActivityListFragment(exerciseId)
            findNavController().navigate(action)
        }
        recommendationAdapter = ExerciseRecommendationAdapter(emptyList()) { exercise ->
            exercise.id?.let { exerciseId ->
                val action = ExerciseFragmentDirections.actionExerciseFragmentToExerciseRecommendationActivityListFragment(exerciseId, "")
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        binding.recyclerViewEnrolledExercises.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = enrollAdapter
        }
        binding.recyclerViewRecommendationClass.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationAdapter
        }
    }

    private fun setupObservers() {
        viewModel.allExercises.observe(viewLifecycleOwner) { exercises ->
            exercises?.forEach { Log.d("ExerciseFragment", it.toString()) }
            categoryAdapter.updateExercises(exercises ?: emptyList())
            allExercisesLoaded = true
            checkAllDataLoaded()
        }

        viewModel.recommendations.observe(viewLifecycleOwner) { exercises ->
            exercises?.forEach { Log.d("ExerciseFragment", it.toString()) }
            recommendationAdapter.updateExercises(exercises ?: emptyList())
            recommendationsLoaded = true
            checkAllDataLoaded()
        }

        viewModel.incompleteExercises.observe(viewLifecycleOwner) { exercisesMyResponseDTO ->
            exercisesMyResponseDTO?.forEach { Log.d("ExerciseFragment", it.toString()) }
            enrollAdapter.updateExercises(exercisesMyResponseDTO)
            myExercisesLoaded = true
            checkAllDataLoaded()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.vHomeHeader.visibility = View.GONE
        binding.TodayActivity.visibility = View.GONE
        binding.recyclerViewCategories.visibility = View.GONE
        binding.recyclerViewEnrolledExercises.visibility = View.GONE
        binding.recyclerViewRecommendationClass.visibility = View.GONE
        binding.tvCategories.visibility = View.GONE
        binding.tvRecommendationClass.visibility = View.GONE
        binding.tvEnrolledExercises.visibility = View.GONE
        binding.tvEnrolledExercisesError.visibility = View.GONE
        binding.tvRecommendationClassError.visibility = View.GONE
        binding.tvCategoriesError.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.vHomeHeader.visibility = View.VISIBLE
        binding.TodayActivity.visibility = View.VISIBLE
    }

    private fun checkAllDataLoaded() {
        if (allExercisesLoaded && recommendationsLoaded && myExercisesLoaded) {
            hideLoading()
            updateUIVisibility()
        }
    }

    private fun updateUIVisibility() {
        if (categoryAdapter.itemCount == 0) {
            binding.tvCategoriesError.visibility = View.VISIBLE
            binding.tvCategories.visibility = View.VISIBLE
            binding.recyclerViewCategories.visibility = View.GONE
        } else {
            binding.tvCategoriesError.visibility = View.GONE
            binding.tvCategories.visibility = View.VISIBLE
            binding.recyclerViewCategories.visibility = View.VISIBLE
        }

        if (enrollAdapter.itemCount == 0) {
            binding.tvEnrolledExercisesError.visibility = View.VISIBLE
            binding.tvEnrolledExercises.visibility = View.VISIBLE
            binding.recyclerViewEnrolledExercises.visibility = View.GONE
        } else {
            binding.tvEnrolledExercisesError.visibility = View.GONE
            binding.tvEnrolledExercises.visibility = View.VISIBLE
            binding.recyclerViewEnrolledExercises.visibility = View.VISIBLE
        }

        if (recommendationAdapter.itemCount == 0) {
            binding.tvRecommendationClassError.visibility = View.VISIBLE
            binding.tvRecommendationClass.visibility = View.VISIBLE
            binding.recyclerViewRecommendationClass.visibility = View.GONE
        } else {
            binding.tvRecommendationClassError.visibility = View.GONE
            binding.tvRecommendationClass.visibility = View.VISIBLE
            binding.recyclerViewRecommendationClass.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.heartsteel.heartory.ui.exercise.recommendation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentExerciseRecommendationActivityListBinding
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.service.model.request.EnrollRequest
import com.heartsteel.heartory.service.repository.ExerciseRepository
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.repository.UserRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import com.heartsteel.heartory.ui.exercise.ExerciseViewModel
import com.heartsteel.heartory.ui.exercise.ViewModelFactory

class ExerciseRecommendationActivityFragment : Fragment() {

    private var _binding: FragmentExerciseRecommendationActivityListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseRecommendationActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel and UserRepository
        val appSharePreference = AppSharePreference(requireContext())
        val tokenManager = JwtRepository(appSharePreference)
        val publicRetrofit = PublicRetrofit()
        val jwtTokenInterceptor = JwtTokenInterceptor(tokenManager, publicRetrofit)
        val privateRetrofit = PrivateRetrofit(jwtTokenInterceptor)
        val repository = ExerciseRepository(privateRetrofit)
        userRepository = UserRepository(FirebaseAuth.getInstance(), appSharePreference, privateRetrofit, publicRetrofit, tokenManager)

        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(repository)).get(ExerciseViewModel::class.java)

        // Fetch recommendations and observe the data
        showLoading()
        setupObservers()
        backClickListener()
    }

    private fun setupObservers() {
        viewModel.recommendations.observe(viewLifecycleOwner, { exercises ->
            exercises?.forEach { Log.d("ExerciseRecommendationActivityFragment", it.toString()) }
            setupRecyclerView(exercises)
            hideLoading()
        })
    }

    private fun setupRecyclerView(exercises: List<Exercise>?) {
        val exerciseList = exercises ?: emptyList()
        val adapter = ExerciseRecommendationActivityAdapter(exerciseList) { exercise ->
            val userId = userRepository.getUserFromSharePref()?.id ?: return@ExerciseRecommendationActivityAdapter
            viewModel.enrollInExercise(exercise.id, EnrollRequest(userId, exercise.id))

            viewModel.enrollmentResponse.observe(viewLifecycleOwner, { response ->
                val enrollmentMessage = if (response.success) {
                    "You have successfully enrolled in the exercise."
                } else {
                    "You are already enrolled in the exercise."
                }

                val action = ExerciseRecommendationActivityFragmentDirections
                    .actionExerciseRecommendationActivityListFragmentToExerciseActivityRecommendationVideoFragment(
                        lessonId = exercise.id,
                        videoUrl = "https://www.youtube.com/embed/-p0PA9Zt8zk",
                        exerciseId = exercise.id,
                        enrollmentMessage = enrollmentMessage
                    )
                findNavController().navigate(action)
            })
        }

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseRecommendationActivityListFragment_to_exerciseFragment)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.vHomeHeader.visibility = View.GONE
        binding.tvClass.visibility = View.GONE
        binding.recyclerViewActivity.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.vHomeHeader.visibility = View.VISIBLE
        binding.tvClass.visibility = View.VISIBLE
        binding.recyclerViewActivity.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
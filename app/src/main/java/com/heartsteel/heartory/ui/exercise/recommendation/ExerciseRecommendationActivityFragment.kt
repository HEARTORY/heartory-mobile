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
import com.heartsteel.heartory.common.util.ToastUtil
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
    private var exerciseId: Int? = null

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

        // Retrieve exercise ID from arguments
        exerciseId = arguments?.getInt("exerciseId")

        showLoading()
        setupObservers()
        backClickListener()
        enrollClickListener()
    }

    private fun setupObservers() {
        viewModel.recommendations.observe(viewLifecycleOwner, { exercises ->
            exercises?.forEach { Log.d("ExerciseRecommendationActivityFragment", it.toString()) }

            exerciseId?.let { id ->
                setupRecyclerView(id, exercises)
            }
            hideLoading()
        })
    }

    private fun setupRecyclerView(exerciseId: Int, exercises: List<Exercise>?) {
        val exerciseList = exercises ?: emptyList()
        val lessons = exerciseList.find { it.id == exerciseId }?.lessons ?: emptyList()
        val adapter = ExerciseRecommendationActivityAdapter(lessons) { lesson ->
            val action = ExerciseRecommendationActivityFragmentDirections
                .actionExerciseRecommendationActivityListFragmentToExerciseActivityRecommendationVideoFragment(
                    lessonId = lesson.id,
                    videoUrl = lesson.videokey ?: "https://www.youtube.com/embed/-p0PA9Zt8zk",
                    exerciseId = exerciseId,
                    enrollmentMessage = ""
                )
            findNavController().navigate(action)
        }

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun enrollClickListener() {
        binding.btnEnroll.setOnClickListener {
            exerciseId?.let { id ->
                val userId = userRepository.getUserFromSharePref()?.id ?: return@setOnClickListener
                viewModel.enrollInExercise(id, EnrollRequest(userId, id))

                viewModel.enrollmentResponse.observe(viewLifecycleOwner, { response ->
                    val message = if (response.success) {
                        "You have successfully enrolled in the exercise."
                    } else {
                        "You are already enrolled in the exercise."
                    }
                    ToastUtil.showToast(requireContext(), message, if (response.success) R.color.green else R.color.red)
                })
            }
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


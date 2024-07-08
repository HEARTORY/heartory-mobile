package com.heartsteel.heartory.ui.exercise.todayactivity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseTodayActivityListBinding
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.repository.ExerciseRepository
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import com.heartsteel.heartory.ui.exercise.ExerciseViewModel
import com.heartsteel.heartory.ui.exercise.ViewModelFactory

class ExerciseTodayActivityFragment : Fragment() {

    private var _binding: FragmentExerciseTodayActivityListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var adapter: ExerciseTodayActivityAdapter
    private var exerciseId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseTodayActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val appSharePreference = AppSharePreference(requireContext())
        val tokenManager = JwtRepository(appSharePreference)
        val publicRetrofit = PublicRetrofit()
        val jwtTokenInterceptor = JwtTokenInterceptor(tokenManager, publicRetrofit)
        val privateRetrofit = PrivateRetrofit(jwtTokenInterceptor)
        val repository = ExerciseRepository(privateRetrofit)
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(repository)).get(ExerciseViewModel::class.java)

        // Get exerciseId from arguments
        exerciseId = arguments?.getInt("exerciseId")
        Log.d("ExerciseTodayActivityFragment", "Received exerciseId: $exerciseId")

        setupObservers()
        setupRecyclerView()
        setupChartView()
        backClickListener()

        viewModel.fetchMyExercises()
    }

    private fun setupRecyclerView() {
        adapter = ExerciseTodayActivityAdapter(emptyList()) { lesson ->
            exerciseId?.let { id ->
                Log.d("ExerciseTodayActivityFragment", "Navigating to detail fragment with exerciseId: $id and videoUrl: ${lesson.videokey}")
                val action = ExerciseTodayActivityFragmentDirections
                    .actionExerciseTodayActivityListFragmentToExerciseTodayDetailExerciseListFragment(
                        videoUrl = lesson.videokey ?: "https://www.youtube.com/embed/-p0PA9Zt8zk",
                        exerciseId = id
                    )
                findNavController().navigate(action)
            }
        }

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ExerciseTodayActivityFragment.adapter
            Log.d("ExerciseTodayActivityFragment", "RecyclerView initialized with LinearLayoutManager and adapter.")
        }
    }

    private fun setupObservers() {
        viewModel.incompleteExercises.observe(viewLifecycleOwner) { exercisesMyResponseDTO ->
            Log.d("ExerciseTodayActivityFragment", "Observed incompleteExercises: ${exercisesMyResponseDTO.size} exercises")
            exercisesMyResponseDTO.forEach { exerciseMyResponseDTO ->
                Log.d("ExerciseTodayActivityFragment", "Exercise ID: ${exerciseMyResponseDTO.exercise.id}, Title: ${exerciseMyResponseDTO.exercise.title}")
            }

            // Filter and extract lessons for the current exerciseId
            val lessons = exercisesMyResponseDTO
                .filter { it.exercise.id == exerciseId }
                .flatMap { it.exercise.lessons ?: emptyList() }

            if (lessons.isEmpty()) {
                Log.d("ExerciseTodayActivityFragment", "No lessons found for exerciseId: $exerciseId")
            } else {
                Log.d("ExerciseTodayActivityFragment", "Found ${lessons.size} lessons for exerciseId: $exerciseId")
            }

            viewModel.myLessons.postValue(lessons)
        }

        viewModel.myLessons.observe(viewLifecycleOwner) { lessons ->
            binding.progressBar.visibility = View.GONE
            binding.vHomeHeader.visibility = View.VISIBLE
            binding.lineChart.visibility = View.VISIBLE
            binding.tvClass.visibility = View.VISIBLE
            binding.recyclerViewActivity.visibility = View.VISIBLE

            adapter.updateLessons(lessons)
        }
    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            Log.d("ExerciseTodayActivityFragment", "Navigating back to ExerciseFragment")
            findNavController().navigate(R.id.action_exerciseTodayActivityListFragment_to_exerciseFragment)
        }
    }

    private fun setupChartView() {
        val chart: LineChart = binding.lineChart
        val entries = mutableListOf<Entry>()

        // Example data for a month
        val daysInMonth = 30
        for (i in 1..daysInMonth) {
            val percentage = (0..100).random().toFloat()
            entries.add(Entry(i.toFloat(), percentage))
        }

        val dataSet = LineDataSet(entries, "Workout Completion").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.BLUE)
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        chart.data = lineData

        // Customizing X-axis
        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        // Customizing Y-axis
        val leftAxis: YAxis = chart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100f
        leftAxis.granularity = 10f
        leftAxis.setDrawGridLines(false)
        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false

        // Refreshing the chart
        chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

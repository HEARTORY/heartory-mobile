package com.heartsteel.heartory.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseActivityListBinding
class ExerciseActivityFragment : Fragment() {

    private var _binding: FragmentExerciseActivityListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        todayActivityClickListener()
        backClickListener()
    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise(name = "Yoga", instructorName = "John Doe"),
            Exercise(name = "Pilates", instructorName = "Jane Smith")
        )
        val adapter = ExerciseActivityAdapter(exerciseList)

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }
    private fun todayActivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseTodayActivityListFragment)

        }
    }
    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseFragment)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
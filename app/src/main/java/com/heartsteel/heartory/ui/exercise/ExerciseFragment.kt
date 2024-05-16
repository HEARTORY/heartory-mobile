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
import com.heartsteel.heartory.databinding.FragmentExerciseBinding


class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        seeAllClickListener()
        todayAvtivityClickListener()

    }

    private fun setupRecyclerView() {
        val exerciseList = listOf(
            Exercise("Yoga"),
            Exercise("Warm Up"),
            Exercise("leg"),
            Exercise("shoulder")
        )
        val adapter = ExerciseCategoryAdapter(exerciseList)

        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }
    private fun seeAllClickListener() {
        binding.tvSeeAllCategories.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseActivityListFragment)

        }
    }
    private fun todayAvtivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseTodayActivityListFragment)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
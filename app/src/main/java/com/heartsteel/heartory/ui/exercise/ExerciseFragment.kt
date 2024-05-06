package com.heartsteel.heartory.ui.exercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentExerciseBinding

class ExerciseFragment : BaseFragment(R.layout.fragment_exercise) {

    private lateinit var _binding: FragmentExerciseBinding
    private val _viewModel: ExerciseViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    private fun setupView() {

    }

    private fun setupEvent() {

    }


}
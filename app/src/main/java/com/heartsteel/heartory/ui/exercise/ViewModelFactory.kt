package com.heartsteel.heartory.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.heartsteel.heartory.service.repository.ExerciseRepository

class ViewModelFactory(private val repository: ExerciseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.heartsteel.heartory.ui.exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.heartsteel.heartory.service.model.domain.Exercise
import com.heartsteel.heartory.service.model.domain.Lesson
import com.heartsteel.heartory.service.model.request.EnrollRequest
import com.heartsteel.heartory.service.model.response.EnrollmentResponseDTO
import com.heartsteel.heartory.service.model.response.ExerciseResponseDTO
import com.heartsteel.heartory.service.model.response.ExerciseMyResponseDTO
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseRepository: ExerciseRepository) : ViewModel() {

    val recommendations = MutableLiveData<List<Exercise>>()
    val completedExercises = MutableLiveData<List<ExerciseMyResponseDTO>>()
    val incompleteExercises = MutableLiveData<List<ExerciseMyResponseDTO>>()
    val myLessons = MutableLiveData<List<Lesson>>()

    private val _enrollmentResponse = MutableLiveData<ResponseObject<EnrollmentResponseDTO>>()
    val enrollmentResponse: LiveData<ResponseObject<EnrollmentResponseDTO>> get() = _enrollmentResponse
    private val _allExercises = MutableLiveData<List<Exercise>?>()
    val allExercises: MutableLiveData<List<Exercise>?> get() = _allExercises

    private val _selectedExerciseId = MutableLiveData<Int>()
    val selectedExerciseId: LiveData<Int> get() = _selectedExerciseId

    fun fetchRecommendations() {
        viewModelScope.launch {
            val response = exerciseRepository.getRecommendations()
            if (response.isSuccessful) {
                response.body()?.data?.let { recommendationsList ->
                    recommendations.postValue(recommendationsList.map { mapToExercise(it) })
                }
            }
        }
    }

    fun fetchMyExercises() {
        viewModelScope.launch {
            val response = exerciseRepository.getMyExercises()
            if (response.isSuccessful) {
                val responseObject = response.body()
                val exerciseDTOs = responseObject?.data ?: emptyList()

                // Separate completed and incomplete exercises
                val completedExercisesList = exerciseDTOs.filter { it.isCompleted }
                val incompleteExercisesList = exerciseDTOs.filter { !it.isCompleted }

                completedExercises.postValue(completedExercisesList)
                incompleteExercises.postValue(incompleteExercisesList)

                // Extract lessons from incomplete exercises
                val lessons = incompleteExercisesList.flatMap { it.exercise.lessons ?: emptyList() }
                myLessons.postValue(lessons)

            } else {
                Log.e("ViewModel", "Failed to fetch exercises")
            }
        }
    }

    fun getLessonForDefaultVideo(): Lesson? {
        val exercises = incompleteExercises.value ?: return null
        for (exerciseDTO in exercises) {
            val targetPosition = exerciseDTO.nextPosition - 1
            val lesson = exerciseDTO.exercise.lessons?.find { it.position == targetPosition }
            if (lesson != null) {
                return lesson
            }
        }
        return null
    }

    fun fetchAllExercises() {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAllExercises()?.map { mapToExercise(it) } ?: emptyList()
            exercises.forEach { exercise ->
                exercise.lessons?.forEach { lesson ->
                }
            }
            allExercises.postValue(exercises)
        }
    }

    fun enrollInExercise(id: Int, enrollRequest: EnrollRequest) {
        viewModelScope.launch {
            try {
                val response = exerciseRepository.enrollExercise(id, enrollRequest)
                if (response.isSuccessful) {
                    _enrollmentResponse.postValue(response.body())
                } else {
                    // Handle error case
                    _enrollmentResponse.postValue(
                        ResponseObject(
                            statusCode = response.code().toString(),
                            message = response.message(),
                            data = null,
                            error = listOf("Failed to enroll"),
                            success = false
                        )
                    )
                }
            } catch (e: Exception) {
                // Handle exception
                _enrollmentResponse.postValue(
                    ResponseObject(
                        statusCode = "500",
                        message = "Internal Server Error",
                        data = null,
                        error = listOf(e.message ?: "Unknown error"),
                        success = false
                    )
                )
            }
        }
    }

    fun selectExerciseId(id: Int) {
        _selectedExerciseId.value = id
    }

    private fun mapToExercise(responseDTO: ExerciseResponseDTO): Exercise {
        return Exercise(
            id = responseDTO.id,
            createdAt = "",
            updatedAt = "",
            title = responseDTO.title,
            subTitle = responseDTO.subTitle ?: "",
            type = responseDTO.type,
            location = responseDTO.location,
            thumbUrl = responseDTO.thumbUrl ?: "",
            isPremium = responseDTO.isPremium,
            lessons = responseDTO.lessons?.map { mapToLesson(it) } ?: emptyList()
        )
    }

    private fun mapToLesson(lessonDTO: Lesson): Lesson {
        return Lesson(
            id = lessonDTO.id,
            createdAt = lessonDTO.createdAt ?: "",
            updatedAt = lessonDTO.updatedAt ?: "",
            lessonName = lessonDTO.lessonName,
            videokey = lessonDTO.videokey ?: "",
            thumbUrl = lessonDTO.thumbUrl ?: "",
            lengthSeconds = lessonDTO.lengthSeconds,
            position = lessonDTO.position
        )
    }
}
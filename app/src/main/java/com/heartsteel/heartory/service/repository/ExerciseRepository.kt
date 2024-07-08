package com.heartsteel.heartory.service.repository

import android.util.Log
import com.google.gson.Gson
import com.heartsteel.heartory.service.api.ExerciseAPI
import com.heartsteel.heartory.service.model.request.EnrollRequest


import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.model.response.ExerciseResponseDTO
import com.heartsteel.heartory.service.model.response.EnrollmentResponseDTO
import com.heartsteel.heartory.service.model.response.ExerciseMyResponseDTO
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val privateRetrofit: PrivateRetrofit

) {
    private var cachedExercises: List<ExerciseResponseDTO>? = null

    private val exerciseAPI: ExerciseAPI by lazy {
        privateRetrofit.exerciseAPI
    }

    suspend fun enrollExercise(id: Int, enrollRequest: EnrollRequest): Response<ResponseObject<EnrollmentResponseDTO>> {
        return privateRetrofit.exerciseAPI.enroll(id, enrollRequest)
    }
    suspend fun getRecommendations(): Response<ResponseObject<List<ExerciseResponseDTO>>> {
        return privateRetrofit.exerciseAPI.getRecommendations()
    }

    suspend fun getMyExercises(): Response<ResponseObject<List<ExerciseMyResponseDTO>>> {
        return privateRetrofit.exerciseAPI.getMyExercises()
    }

    suspend fun getAllExercises(): Response<ResponseObject<List<ExerciseResponseDTO>>> {
        return privateRetrofit.exerciseAPI.getAllExercises()
    }

}
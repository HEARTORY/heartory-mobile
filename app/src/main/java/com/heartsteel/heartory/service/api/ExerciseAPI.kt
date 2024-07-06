package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.request.EnrollRequest
import com.heartsteel.heartory.service.model.response.ExerciseResponseDTO
import com.heartsteel.heartory.service.model.response.EnrollmentResponseDTO
import com.heartsteel.heartory.service.model.response.ExerciseMyResponseDTO
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExerciseAPI {

    @POST("exercise/{id}/enroll")
    suspend fun enroll(@Path("id") id: Int, @Body enrollRequest: EnrollRequest): Response<ResponseObject<EnrollmentResponseDTO>>

    @GET("exercise/recommendation")
    suspend fun getRecommendations(): Response<ResponseObject<List<ExerciseResponseDTO>>>

    @GET("exercise/my-exercise")
    suspend fun getMyExercises(): Response<ResponseObject<List<ExerciseMyResponseDTO>>>

    @GET("exercise/all")
    suspend fun getAllExercises(): Response<ResponseObject<List<ExerciseResponseDTO>>>

}
package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.SetProfileForOnBoardReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id")id: Int): Response<ResponseObject<User>>

    @POST("users")
    suspend fun createUser(user: User): Response<ResponseObject<User>>

    @PATCH("users/{id}")
    suspend fun updateUserForOnboarding(@Path("id")id: Int,@Body user: User): Response<ResponseObject<User>>

}
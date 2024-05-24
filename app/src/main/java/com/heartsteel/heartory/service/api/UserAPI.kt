package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.model.domain.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAPI {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id")id: Long): Response<ResponseObject<User>>

}
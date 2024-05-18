package com.heartsteel.heartory.data.api

import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.LoginRes
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq): Response<ResponseObject<LoginRes>>

    @POST("auth/signup")
    suspend fun register(@Body registerReq: RegisterReq): Response<ResponseObject<Int>>

    @POST("auth/isEmailExist")
    suspend fun isEmailIsExist(@Body email: String): Response<ResponseObject<Boolean>>
}
package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.request.ForgotPasswordReq
import com.heartsteel.heartory.service.model.request.IsEmailExistReq
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.service.model.response.LoginRes
import com.heartsteel.heartory.service.model.request.RefreshTokenReq
import com.heartsteel.heartory.service.model.request.RegisterReq
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPI {

    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq): Response<ResponseObject<LoginRes>>

    @POST("auth/signup")
    suspend fun register(@Body registerReq: RegisterReq): Response<ResponseObject<Int>>

    @POST("auth/isEmailExist")
    suspend fun isEmailIsExist(@Body isEmailExistReq: IsEmailExistReq): Response<ResponseObject<Boolean>>

    @POST("auth/forget-password")
    suspend fun forgotPassword(@Body forgotPasswordReq: ForgotPasswordReq): Response<ResponseObject<Int>>

    @POST("auth/refreshToken")
    suspend fun refreshToken(@Body refreshTokenReq: RefreshTokenReq): Response<ResponseObject<LoginRes>>
}
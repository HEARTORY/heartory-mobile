package com.heartsteel.heartory.data.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.LoginRes
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
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

    @GET("auth/isEmailExist")
    suspend fun isEmailIsExist(@Query(value = "email") email: String): Response<ResponseObject<Boolean>>
}
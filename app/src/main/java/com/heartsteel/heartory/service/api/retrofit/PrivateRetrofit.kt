package com.heartsteel.heartory.service.api.retrofit

import com.heartsteel.heartory.common.constant.ApiConstant
import com.heartsteel.heartory.service.api.UserAPI
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class PrivateRetrofit @Inject constructor(
    private val jwtTokenInterceptor: JwtTokenInterceptor
) {

    private val privateRetrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(jwtTokenInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val userApi by lazy {
        privateRetrofit.create(UserAPI::class.java)
    }

}
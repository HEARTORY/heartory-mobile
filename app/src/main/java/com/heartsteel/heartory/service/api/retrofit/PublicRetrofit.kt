package com.heartsteel.heartory.service.api.retrofit


import com.heartsteel.heartory.common.constant.ApiConstant
import com.heartsteel.heartory.service.api.AuthAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class PublicRetrofit @Inject constructor() {

    private val publicRetrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    val authApi by lazy {
        publicRetrofit.create(AuthAPI::class.java)
    }

}
package com.heartsteel.heartory.service.api.retrofit

import com.google.gson.GsonBuilder
import com.heartsteel.heartory.common.constant.ApiConstant
import com.heartsteel.heartory.service.api.ArticleApi
import com.heartsteel.heartory.service.api.HBRecordAPI
import com.heartsteel.heartory.service.api.MessageAPI
import com.heartsteel.heartory.service.api.PaymentAPI
import com.heartsteel.heartory.service.api.UserAPI
import com.heartsteel.heartory.service.api.ExerciseAPI
import com.heartsteel.heartory.service.jwt.JwtTokenInterceptor
import com.heartsteel.heartory.service.model.domain.HBRecord
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
            .addNetworkInterceptor(logging)
            .addInterceptor(jwtTokenInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val userApi by lazy {
        privateRetrofit.create(UserAPI::class.java)
    }

    val hBRecordAPI by lazy {
        privateRetrofit.create(HBRecordAPI::class.java)
    }

    val paymentAPI by lazy {
        privateRetrofit.create(PaymentAPI::class.java)
    }

    val messageAPI by lazy {
        privateRetrofit.create(MessageAPI::class.java)
    }
    val exerciseAPI by lazy {
        privateRetrofit.create(ExerciseAPI::class.java)
    }

    val articleAPI by lazy {
        privateRetrofit.create(ArticleApi::class.java)
    }

}
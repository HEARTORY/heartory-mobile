package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.domain.Message
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageAPI {

    @GET("message/self")
    suspend fun getMessages(@Query("page") page: Int): Response<ResponseObject<MutableList<Message>>>

    @POST("message")
    suspend fun sendMessage(@Body message: Message): Response<ResponseObject<Message>>
}
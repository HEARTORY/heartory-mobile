package com.heartsteel.heartory.service.api

import com.google.gson.JsonObject
import com.heartsteel.heartory.service.model.domain.Subscription
import com.heartsteel.heartory.service.model.request.CreateOrderReq
import com.heartsteel.heartory.service.model.response.CreateOrderRes
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentAPI {

    @POST("order/")
    suspend fun createOrder(@Body createOrderReq: CreateOrderReq): Response<ResponseObject<CreateOrderRes>>

    @GET("subscriptions")
    suspend fun getSubscription(): Response<ResponseObject<List<Subscription>>>
}
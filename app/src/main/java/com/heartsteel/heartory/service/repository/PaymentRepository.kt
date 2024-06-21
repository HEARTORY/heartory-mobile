package com.heartsteel.heartory.service.repository

import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.model.domain.Subscription
import com.heartsteel.heartory.service.model.request.CreateOrderReq
import com.heartsteel.heartory.service.model.response.CreateOrderRes
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val privateRetrofit: PrivateRetrofit
) {
    suspend fun createOrder(createOrderReq: CreateOrderReq): Response<ResponseObject<CreateOrderRes>> {
        return privateRetrofit.paymentAPI.createOrder(createOrderReq)
    }

    suspend fun getSubscription(): Response<ResponseObject<List<Subscription>>> {
        return privateRetrofit.paymentAPI.getSubscription()
    }
}
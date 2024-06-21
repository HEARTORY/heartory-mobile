package com.heartsteel.heartory.service.repository

import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.model.domain.Message
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val privateRetrofit: PrivateRetrofit
) {

    suspend fun getMessages(page: Int) = privateRetrofit.messageAPI.getMessages(page)

    suspend fun sendMessage(message: Message) = privateRetrofit.messageAPI.sendMessage(message)
}
package com.heartsteel.heartory.service.model.domain

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Message(
    val id: Int? = null,
    val content: String,
    val userId: String? = null,
    val role: String,
    val createdAt: String? = ZonedDateTime.now().toString(),
){

}
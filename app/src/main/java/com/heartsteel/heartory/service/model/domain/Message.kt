package com.heartsteel.heartory.service.model.domain

data class Message(
    val id: Int? = null,
    val content: String,
    val userId: String? = null,
    val role: String,
    val createdAt: Long? = System.currentTimeMillis(),
){

}
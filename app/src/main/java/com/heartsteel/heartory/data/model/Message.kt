package com.heartsteel.heartory.data.model

data class Message(
    val id: String,
    val content: String,
    val userId: String? = null,
    val isByUser: Boolean = false,
    val timeStamp: Long? = System.currentTimeMillis(),
    val sent: Boolean? = false,
    val seen: Boolean?= false
)
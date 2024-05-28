package com.heartsteel.heartory.service.model.domain

data class Message(
    val content: String,
    val userId: String? = null,
    val isByUser: Boolean = false,
    val timeStamp: Long? = System.currentTimeMillis(),
    val sent: Boolean? = false,
    val seen: Boolean? = false
) : BaseEntity() {

}
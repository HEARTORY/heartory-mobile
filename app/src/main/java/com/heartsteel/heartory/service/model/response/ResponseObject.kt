package com.heartsteel.heartory.service.model.response

data class ResponseObject<T>(
    val statusCode: String,
    val message: String,
    val data: T?,
    val error: List<String>?,
    val success: Boolean
) {
}
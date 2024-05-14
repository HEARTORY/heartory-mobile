package com.heartsteel.heartory.data.model

data class ResponseObject<T>(
    val statusCode: String,
    val message: String,
    val data: T?,
    val error: List<String>?,
    val success: Boolean
) {
}
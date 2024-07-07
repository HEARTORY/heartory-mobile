package com.heartsteel.heartory.service.model.domain

data class Subscription(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val durationDays: Int
) {
}
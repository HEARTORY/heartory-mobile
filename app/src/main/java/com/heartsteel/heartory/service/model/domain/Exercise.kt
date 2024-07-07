package com.heartsteel.heartory.service.model.domain

data class Exercise(
    val id: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val title: String,
    val subTitle: String?,
    val type: String,
    val location: String,
    val thumbUrl: String?,
    val isPremium: Boolean,
    val lessons: List<Lesson>?
)
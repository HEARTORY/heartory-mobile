package com.heartsteel.heartory.service.model.domain

data class Lesson(
    val id: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val lessonName: String,
    val videokey: String?,
    val thumbUrl: String?,
    val lengthSeconds: Int,
    val position: Int
)
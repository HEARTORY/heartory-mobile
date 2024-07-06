package com.heartsteel.heartory.service.model.response

import com.heartsteel.heartory.service.model.domain.Lesson

data class ExerciseResponseDTO(
    val id: Int,
    val title: String,
    val subTitle: String?,
    val type: String,
    val location: String,
    val thumbUrl: String?,
    val isPremium: Boolean,
    val lessons: List<Lesson>?
)
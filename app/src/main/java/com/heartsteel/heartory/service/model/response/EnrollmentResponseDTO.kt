package com.heartsteel.heartory.service.model.response

import com.heartsteel.heartory.service.model.domain.Exercise

data class EnrollmentResponseDTO(
    val id: Int,
    val exercise: Exercise,
    val isCompleted: Boolean,
    val nextPosition: Int,
    val progress: Int
)

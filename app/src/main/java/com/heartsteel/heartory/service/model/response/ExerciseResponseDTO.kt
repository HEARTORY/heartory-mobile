package com.heartsteel.heartory.service.model.response

import com.google.gson.annotations.SerializedName
import com.heartsteel.heartory.service.model.domain.Lesson

data class ExerciseResponseDTO(
    val id: Int,
    val title: String,
    val subTitle: String?,
    val type: String,
    val location: String,
    @SerializedName(value = "thumbUrl", alternate = ["thumbURl"])
    val thumbUrl: String?,
    val isPremium: Boolean,
    val lessons: List<Lesson>?
)
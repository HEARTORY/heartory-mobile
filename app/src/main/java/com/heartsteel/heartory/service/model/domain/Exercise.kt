package com.heartsteel.heartory.service.model.domain

import com.google.gson.annotations.SerializedName

data class Exercise(
    val id: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val title: String,
    val subTitle: String?,
    val type: String,
    val location: String,
    @SerializedName(value = "thumbUrl", alternate = ["thumbURl"])
    val thumbUrl: String?,
    val isPremium: Boolean,
    val lessons: List<Lesson>?
)
package com.heartsteel.heartory.service.model.domain

import com.google.gson.annotations.SerializedName

data class Lesson(
    val id: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val lessonName: String,
    val videokey: String?,
    @SerializedName(value = "thumbUrl", alternate = ["thumbURl"])
    val thumbUrl: String?,
    val lengthSeconds: Int,
    val position: Int
)
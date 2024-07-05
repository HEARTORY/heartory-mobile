package com.heartsteel.heartory.service.model.domain

import java.util.Date

data class Article (
    val id: String? = null,
    val title: String? = null,
    val content : String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val imageUrl: String? = null,
    )



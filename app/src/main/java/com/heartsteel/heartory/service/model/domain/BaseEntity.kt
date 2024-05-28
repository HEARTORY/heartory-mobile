package com.heartsteel.heartory.service.model.domain

import java.util.Date

open class BaseEntity (
    val id: Int? = null,
    val isDeleted: Boolean = false,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
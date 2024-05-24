package com.heartsteel.heartory.service.model.domain

import java.util.Date

open class BaseEntity (
    val id: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
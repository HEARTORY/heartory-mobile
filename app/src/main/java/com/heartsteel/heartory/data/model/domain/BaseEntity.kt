package com.heartsteel.heartory.data.model.domain

import java.util.Date

open class BaseEntity (
    val id: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
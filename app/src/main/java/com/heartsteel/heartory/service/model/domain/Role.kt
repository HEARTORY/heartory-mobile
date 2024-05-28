package com.heartsteel.heartory.service.model.domain

data class Role(
    val roleTitle: String,
) : BaseEntity() {

    override fun toString(): String {
        return "Role(id=$id, roleTitle=$roleTitle)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role
        if (id != other.id) return false

        return true
    }
}
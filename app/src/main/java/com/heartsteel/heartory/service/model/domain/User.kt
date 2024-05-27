package com.heartsteel.heartory.service.model.domain

data class User(
    val role: Role?,
    val email: String?,
    var firstName: String?,
    var lastName: String?,
    var phone: String?,
    var secondName: String?,
    var avatar: String?,
    var dateOfBirth: String?,
    var stanceId: Int?,
) : BaseEntity() {

    override fun toString(): String {
        return "User(id=$id, role=$role, email=$email, firstName=$firstName, lastName=$lastName, phone=$phone, secondName=$secondName, avatar=$avatar, dateOfBirth=$dateOfBirth, stanceId=$stanceId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User
        if (id != other.id) return false

        return true
    }


}
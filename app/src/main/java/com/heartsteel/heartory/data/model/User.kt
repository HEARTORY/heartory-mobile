package com.heartsteel.heartory.data.model

data class User(
    val id: Int?,
    val role: String?,
    val email: String?,
    var firstName: String?,
    var lastName: String?,
    var phone: String?,
    var secondName: String?,
    var avatar: String?,
    var dateOfBirth: String?,
    var refreshToken: String?,
    var stanceId: Int?,
) {

    override fun toString(): String {
        return "User(id=$id, role=$role, email=$email, firstName=$firstName, lastName=$lastName, phone=$phone, secondName=$secondName, avatar=$avatar, dateOfBirth=$dateOfBirth, refreshToken=$refreshToken, stanceId=$stanceId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (role?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (secondName?.hashCode() ?: 0)
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + (dateOfBirth?.hashCode() ?: 0)
        result = 31 * result + (refreshToken?.hashCode() ?: 0)
        result = 31 * result + (stanceId ?: 0)
        return result
    }
}
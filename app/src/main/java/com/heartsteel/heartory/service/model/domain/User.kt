package com.heartsteel.heartory.service.model.domain

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: Int? = null,
    val role: Role?,
    val email: String?,
    var firstName: String?,
    var lastName: String?,
    var phone: String?,
    var secondName: String?,
    var avatar: String?,
    var dateOfBirth: String?,
    var stanceId: Int?,
    var gender: String?,
    var weight: Double?,
    var height: Double?,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Role::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun toString(): String {
        return "User(id=$id, role=$role, email=$email, firstName=$firstName, lastName=$lastName, phone=$phone, secondName=$secondName, avatar=$avatar, dateOfBirth=$dateOfBirth, stanceId=$stanceId)"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id ?: 0)
        dest.writeParcelable(role, flags)
        dest.writeString(email)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(phone)
        dest.writeString(secondName)
        dest.writeString(avatar)
        dest.writeString(dateOfBirth)
        dest.writeInt(stanceId!!)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User
        if (id != other.id) return false

        return true
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
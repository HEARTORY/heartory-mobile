package com.heartsteel.heartory.service.model.domain

import android.os.Parcel
import android.os.Parcelable

data class Role(
    val id: Int? = null,
    val roleTitle: String?,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(roleTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Role> {
        override fun createFromParcel(parcel: Parcel): Role {
            return Role(parcel)
        }

        override fun newArray(size: Int): Array<Role?> {
            return arrayOfNulls(size)
        }
    }


}
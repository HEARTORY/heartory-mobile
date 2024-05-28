package com.heartsteel.heartory.service.model.domain

import android.os.Parcel
import android.os.Parcelable

data class Role(
    val roleTitle: String?,
) : BaseEntity(), Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun toString(): String {
        return "Role(id=$id, roleTitle=$roleTitle)"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id ?: 0)
        dest.writeString(roleTitle)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role
        if (id != other.id) return false

        return true
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
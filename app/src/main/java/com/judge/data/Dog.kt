package com.judge.data

import android.os.Parcel
import android.os.Parcelable

data class Dog(
    val id: Long,
    val name: String?,
    val breeds: String?,
    val imageUrl: String?,
    val description: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
        writeString(breeds)
        writeString(imageUrl)
        writeString(description)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Dog> = object : Parcelable.Creator<Dog> {
            override fun createFromParcel(source: Parcel): Dog = Dog(source)
            override fun newArray(size: Int): Array<Dog?> = arrayOfNulls(size)
        }
    }
}
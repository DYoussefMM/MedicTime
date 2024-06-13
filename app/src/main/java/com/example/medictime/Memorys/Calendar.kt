package com.example.medictime.Memorys

import android.os.Parcel
import android.os.Parcelable

data class Calendar(
    val title: String,
    val descripcion: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Calendar> {
        override fun createFromParcel(parcel: Parcel): Calendar {
            return Calendar(parcel)
        }

        override fun newArray(size: Int): Array<Calendar?> {
            return arrayOfNulls(size)
        }
    }
}
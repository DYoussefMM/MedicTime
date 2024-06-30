package com.example.medictime.memorys

import android.os.Parcel
import android.os.Parcelable

@Suppress("unused")
data class Calendar(
    val title: String = "",
    val descripcion: String = "",
    val creator: String = "",
    var events: MutableList<Event> = mutableListOf(),
    var isShared: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<Event>().apply { parcel.readTypedList(this, Event.CREATOR) },
        parcel.readByte() != 0.toByte()
    )

    // Constructor sin argumentos añadido para Firebase Firestore
    constructor() : this("", "", "", mutableListOf(), false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(descripcion)
        parcel.writeString(creator)
        parcel.writeTypedList(events)
        parcel.writeByte(if (isShared) 1 else 0)
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

    data class Event(
        val title: String,
        val description: String,
        val date: Long,
        val medicinas: String // Nueva propiedad para medicinas
    ) : Parcelable {

        constructor() : this("", "", 0, "")

        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeLong(date)
            parcel.writeString(medicinas)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Event> {
            override fun createFromParcel(parcel: Parcel): Event {
                return Event(parcel)
            }

            override fun newArray(size: Int): Array<Event?> {
                return arrayOfNulls(size)
            }
        }
    }
}
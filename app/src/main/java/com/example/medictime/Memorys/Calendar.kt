package com.example.medictime.Memorys

import android.os.Parcel
import android.os.Parcelable

@Suppress("unused")
data class Calendar(
    val title: String = "",
    val descripcion: String = "",
    val creator: String = "", // Añadir este campo
    var events: MutableList<Event> = mutableListOf() // Lista de eventos
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
        parcel.readTypedList(events, Event.CREATOR)
    }

    // Constructor sin argumentos añadido para Firebase Firestore
    constructor() : this("", "", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(descripcion)
        parcel.writeString(creator)
        parcel.writeTypedList(events)
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
        val date: Long
    ) : Parcelable {

        constructor() : this("", "", 0) // Constructor sin argumentos

        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeLong(date)
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





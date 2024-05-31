package com.example.medictime.Memorys

import android.os.Parcel
import android.os.Parcelable

data class Perfil(
    val nombre: String,
    val correo: String,
    val edad: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(correo)
        parcel.writeInt(edad)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Perfil> {
        override fun createFromParcel(parcel: Parcel): Perfil {
            return Perfil(parcel)
        }

        override fun newArray(size: Int): Array<Perfil?> {
            return arrayOfNulls(size)
        }
    }
}
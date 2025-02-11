package com.example.deber1.models

import android.os.Parcel
import android.os.Parcelable

data class Empleado(
    var nombre: String,
    var apellido: String,
    var edad: Int,
    var departamento: String,
    var salario: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeInt(edad)
        parcel.writeString(departamento)
        parcel.writeDouble(salario)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Empleado> {
        override fun createFromParcel(parcel: Parcel): Empleado {
            return Empleado(parcel)
        }

        override fun newArray(size: Int): Array<Empleado?> {
            return arrayOfNulls(size)
        }
    }
}
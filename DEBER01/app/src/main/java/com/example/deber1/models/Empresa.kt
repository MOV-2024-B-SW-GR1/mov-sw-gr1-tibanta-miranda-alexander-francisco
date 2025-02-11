package com.example.deber1.models

import android.os.Parcel
import android.os.Parcelable

data class Empresa(
    val nombre: String,
    val cantidadEmpleados: Int,
    val direccion: String,
    var empleados: MutableList<Empleado> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        mutableListOf<Empleado>().apply {
            parcel.readList(this, Empleado::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeInt(cantidadEmpleados)
        parcel.writeString(direccion)
        parcel.writeList(empleados)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Empresa> {
        override fun createFromParcel(parcel: Parcel): Empresa {
            return Empresa(parcel)
        }

        override fun newArray(size: Int): Array<Empresa?> {
            return arrayOfNulls(size)
        }
    }
}
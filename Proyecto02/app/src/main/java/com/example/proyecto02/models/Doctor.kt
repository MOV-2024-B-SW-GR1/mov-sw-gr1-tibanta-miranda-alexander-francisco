package com.example.proyecto02.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctores")
data class Doctor(
    @PrimaryKey val cedula: String,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val departamento: String,
    val especialidad: String
)
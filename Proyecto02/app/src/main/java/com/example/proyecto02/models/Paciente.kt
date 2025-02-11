package com.example.proyecto02.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pacientes")
data class Paciente(
    @PrimaryKey val cedula: String,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val peso: Double,
    val altura: Double
)
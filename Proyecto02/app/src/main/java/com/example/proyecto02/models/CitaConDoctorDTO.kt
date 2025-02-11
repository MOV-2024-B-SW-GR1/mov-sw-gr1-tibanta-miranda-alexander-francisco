package com.example.proyecto02.models

data class CitaConDoctorDTO(
    val id: Int,
    val fecha: String,
    val hora: String,
    val paciente_id: String,
    val doctor_id: String,
    val nombreDoctor: String,
    val apellidoDoctor: String
)
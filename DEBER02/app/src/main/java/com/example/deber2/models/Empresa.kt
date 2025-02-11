package com.example.deber2.models

data class Empresa(
    val id: String = 0.toString(), // ID autoincremental en la base de datos
    val nombre: String,
    val cantidadEmpleados: String,
    val direccion: String
)
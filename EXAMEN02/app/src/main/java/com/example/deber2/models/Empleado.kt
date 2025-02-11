package com.example.deber2.models

data class Empleado(
    val id: Int = 0, // ID autoincremental
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val departamento: String,
    val salario: Double,
    val empresaId: Int // Relacionado con la empresa
)
package com.example.sw2024bgr1_aftm

class BEntrenador(
    var id: Int,
    var nombre: String,
    var descripcion: String?
) {
    override fun toString(): String {
        return "$nombre ${descripcion}"
    }
}
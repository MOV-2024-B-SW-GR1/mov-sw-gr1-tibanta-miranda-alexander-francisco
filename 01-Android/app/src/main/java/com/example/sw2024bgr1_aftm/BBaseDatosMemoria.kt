package com.example.sw2024bgr1_aftm

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Alexander", "a.t@a.com"))
            arregloBEntrenador.add(BEntrenador(1,"Francisco", "f.t@a.com"))
            arregloBEntrenador.add(BEntrenador(1,"Daniela", "d.t@a.com"))
        }
    }
}
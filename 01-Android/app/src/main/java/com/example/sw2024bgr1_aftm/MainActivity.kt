package com.example.sw2024bgr1_aftm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCicloVida = findViewById<Button>(R.id.button)
        botonCicloVida
            .setOnClickListener{
                irActividad(ACicloVida::class.java)
            }
    }

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this, clase))
    }
}
package com.example.proyecto02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto02.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIniciar = findViewById<Button>(R.id.btnIniciar)

        // Acción para el botón de Login
        btnIniciar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
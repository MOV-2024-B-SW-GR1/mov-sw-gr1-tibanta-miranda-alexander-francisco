package com.example.proyecto02.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto02.R
import com.example.proyecto02.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorHomeActivity : AppCompatActivity() {

    private lateinit var lvCitasDoctor: ListView
    private lateinit var database: AppDatabase
    private var doctorId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_home)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenidaDoctor)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesionDoctor)
        lvCitasDoctor = findViewById(R.id.lvCitasAgendadas)
        database = AppDatabase.getDatabase(this)

        // Obtener doctorId desde el intent
        doctorId = intent.getStringExtra("doctorId") ?: ""

        // Cargar citas del doctor
        cargarCitas()

        // Obtener el nombre del doctor desde el intent
        val nombreDoctor = intent.getStringExtra("nombreDoctor") ?: "Doctor"
        txtBienvenida.text = "Bienvenido, Dr. $nombreDoctor"

        btnCerrarSesion.setOnClickListener {
            finish()
        }
    }

    private fun cargarCitas() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val citas = database.citaDAO().obtenerCitasPorDoctor(doctorId)

                // Volver al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    if (citas.isEmpty()) {
                        Toast.makeText(this@DoctorHomeActivity, "No tienes citas agendadas", Toast.LENGTH_SHORT).show()
                        return@withContext
                    }

                    val citasInfo = citas.map {
                        "Fecha: ${it.fecha}, Hora: ${it.hora}, Paciente: ${it.paciente_id}"
                    }

                    val adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_list_item_1, citasInfo)
                    lvCitasDoctor.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DoctorHomeActivity, "Error al cargar citas: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
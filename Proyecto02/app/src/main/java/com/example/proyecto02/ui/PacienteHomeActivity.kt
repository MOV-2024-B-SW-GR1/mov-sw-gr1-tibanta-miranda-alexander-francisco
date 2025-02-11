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
import kotlinx.coroutines.launch

class PacienteHomeActivity : AppCompatActivity() {

    private lateinit var lvCitasAgendadas: ListView
    private lateinit var database: AppDatabase
    private var pacienteId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_home)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenidaPaciente)
        val btnAgendarCita = findViewById<Button>(R.id.btnAgendarCita)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesionPaciente)
        lvCitasAgendadas = findViewById(R.id.lvCitasAgendadas)
        database = AppDatabase.getDatabase(this)

        pacienteId = intent.getStringExtra("pacienteId") ?: ""

        cargarCitas()

        // Obtener el nombre del paciente y usar un valor por defecto si es null
        val nombrePaciente = intent.getStringExtra("nombrePaciente") ?: "Paciente"
        txtBienvenida.text = "Bienvenido, $nombrePaciente"

        btnAgendarCita.setOnClickListener {
            val intent = Intent(this@PacienteHomeActivity, AgendarCitaActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            finish()
        }
    }

    private fun cargarCitas() {
        lifecycleScope.launch {
            try {
                val citas = database.citaDAO().obtenerCitasPorPaciente(pacienteId)
                if (citas.isEmpty()) {
                    Toast.makeText(this@PacienteHomeActivity, "No tienes citas agendadas", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val citasInfo = citas.map {
                    "Fecha: ${it.fecha}, Hora: ${it.hora}, Doctor: ${it.doctor_id}"
                }
                val adapter = ArrayAdapter(this@PacienteHomeActivity, android.R.layout.simple_list_item_1, citasInfo)
                lvCitasAgendadas.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@PacienteHomeActivity, "Error al cargar citas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
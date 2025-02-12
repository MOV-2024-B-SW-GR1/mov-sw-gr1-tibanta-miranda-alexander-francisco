package com.example.proyecto02.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto02.R
import com.example.proyecto02.database.AppDatabase
import com.example.proyecto02.models.Cita
import kotlinx.coroutines.launch

class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var edtFecha: EditText
    private lateinit var edtHora: EditText
    private lateinit var spinnerDoctores: Spinner
    private lateinit var btnAgendar: Button
    private lateinit var btnEliminar: Button
    private lateinit var database: AppDatabase

    private var pacienteId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)

        database = AppDatabase.getDatabase(this)

        edtFecha = findViewById(R.id.edtFecha)
        edtHora = findViewById(R.id.edtHora)
        spinnerDoctores = findViewById(R.id.spinnerDoctores)
        btnAgendar = findViewById(R.id.btnAgendar)
        btnEliminar = findViewById(R.id.btnEliminarCita)

        pacienteId = intent.getStringExtra("pacienteId") ?: ""

        cargarDoctores()

        btnAgendar.setOnClickListener {
            agendarCita()
        }

        btnEliminar.setOnClickListener {
            eliminarCita()
        }
    }

    private fun cargarDoctores() {
        lifecycleScope.launch {
            try {
                val doctores = database.doctorDAO().obtenerTodosLosDoctores()
                if (doctores.isEmpty()) {
                    Toast.makeText(this@AgendarCitaActivity, "No hay doctores disponibles", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val nombresDoctores = doctores.map { it.nombre }
                val adapter = ArrayAdapter(this@AgendarCitaActivity, android.R.layout.simple_spinner_item, nombresDoctores)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDoctores.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@AgendarCitaActivity, "Error al cargar doctores: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun agendarCita() {
        val fecha = edtFecha.text.toString().trim()
        val hora = edtHora.text.toString().trim()
        val doctorNombre = spinnerDoctores.selectedItem?.toString()

        // Validar campos
        if (fecha.isEmpty() || hora.isEmpty() || doctorNombre.isNullOrEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!validarFecha(fecha)) {
            Toast.makeText(this, "Fecha inválida. Use el formato YYYY-MM-DD", Toast.LENGTH_SHORT).show()
            return
        }

        if (!validarHora(hora)) {
            Toast.makeText(this, "Hora inválida. Use el formato HH:MM", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val doctor = database.doctorDAO().obtenerDoctorPorNombre(doctorNombre)
                if (doctor == null) {
                    Toast.makeText(this@AgendarCitaActivity, "Doctor no encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                // Asignar doctorId
                val doctorId = doctor.cedula ?: ""
                val nuevaCita = Cita(fecha = fecha, hora = hora, paciente_id = pacienteId, doctor_id = doctorId)
                database.citaDAO().insertarCita(nuevaCita)
                Toast.makeText(this@AgendarCitaActivity, "Cita agendada", Toast.LENGTH_SHORT).show()

                // Regresar a PacienteHomeActivity después de agendar la cita
                val intent = Intent(this@AgendarCitaActivity, PacienteHomeActivity::class.java).apply {
                    putExtra("pacienteId", pacienteId)
                    putExtra("nombrePaciente", intent.getStringExtra("nombrePaciente")) // Mantener nombre del paciente
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AgendarCitaActivity, "Error al agendar cita: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun eliminarCita() {
        val fecha = edtFecha.text.toString().trim()
        val hora = edtHora.text.toString().trim()

        if (fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Ingrese fecha y hora", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                database.citaDAO().eliminarCita(pacienteId, fecha, hora)
                Toast.makeText(this@AgendarCitaActivity, "Cita eliminada", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@AgendarCitaActivity, PacienteHomeActivity::class.java).apply {
                    putExtra("pacienteId", pacienteId)
                    putExtra("nombrePaciente", intent.getStringExtra("nombrePaciente")) // Mantener nombre del paciente
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AgendarCitaActivity, "Error al eliminar cita: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Validar fecha con el formato YYYY-MM-DD
    fun validarFecha(fecha: String): Boolean {
        val regexFecha = Regex("^(\\d{4})-(\\d{2})-(\\d{2})$")
        return fecha.matches(regexFecha)
    }

    // Validar hora con el formato HH:MM
    fun validarHora(hora: String): Boolean {
        val regexHora = Regex("^(\\d{2}):(\\d{2})$")
        return hora.matches(regexHora)
    }

}
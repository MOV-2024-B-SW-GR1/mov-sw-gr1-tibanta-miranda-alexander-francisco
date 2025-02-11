package com.example.proyecto02.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class DoctorHomeActivity : AppCompatActivity() {

    private lateinit var lvCitasAgendadas: ListView
    private lateinit var database: AppDatabase
    private lateinit var btnCerrarSesion: Button
    private var doctorId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_home)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenidaDoctor)
        lvCitasAgendadas = findViewById(R.id.lvCitasAgendadas)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesionDoctor)

        database = AppDatabase.getDatabase(this)

        doctorId = intent.getStringExtra("doctorId") ?: ""  // Se obtiene el doctorId

        // Obtener el nombre del doctor y usar un valor por defecto si es null
        val nombreDoctor = intent.getStringExtra("nombreDoctor") ?: "Doctor"
        txtBienvenida.text = "Bienvenido, $nombreDoctor"

        // Llamar a la función para cargar las citas automáticamente al iniciar la actividad
        cargarCitas()

        // Configurar el botón de cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cargarCitas() {
        lifecycleScope.launch {
            try {
                val citasConDoctor = database.citaDAO().obtenerCitasPorDoctor(doctorId)
                Log.d("DoctorHome", "Citas encontradas: ${citasConDoctor.size}")

                if (citasConDoctor.isEmpty()) {
                    Toast.makeText(this@DoctorHomeActivity, "No tienes citas agendadas", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val citasInfo = citasConDoctor.map { cita ->
                    "Fecha: ${cita.fecha}, Hora: ${cita.hora}, Paciente: ${cita.paciente_id}, Doctor: ${cita.nombreDoctor} ${cita.apellidoDoctor}"
                }

                val adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_list_item_1, citasInfo)
                lvCitasAgendadas.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@DoctorHomeActivity, "Error al cargar citas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cerrarSesion() {
        // Aquí puedes borrar los datos de sesión (si es necesario) y redirigir al usuario
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Esto asegura que la actividad actual (DoctorHomeActivity) se cierre
    }
}
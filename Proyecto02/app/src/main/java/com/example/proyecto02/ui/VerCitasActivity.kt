package com.example.proyecto02.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto02.R
import com.example.proyecto02.database.AppDatabase
import com.example.proyecto02.models.Cita
import com.example.proyecto02.models.CitaConDoctorDTO
import kotlinx.coroutines.launch

class VerCitasActivity : AppCompatActivity() {

    private lateinit var recyclerCitas: LinearLayout // Usamos LinearLayout en lugar de RecyclerView
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_citas)

        recyclerCitas = findViewById(R.id.recyclerCitas) // Aquí obtenemos el LinearLayout
        recyclerCitas.orientation = LinearLayout.VERTICAL // Establecemos la orientación

        database = AppDatabase.getDatabase(this)

        val doctorId = intent.getStringExtra("doctorId")

        if (doctorId.isNullOrEmpty()) {
            Toast.makeText(this, "Doctor no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarCitas(doctorId)
    }

    private fun cargarCitas(doctorId: String) {
        lifecycleScope.launch {
            try {
                val citas = database.citaDAO().obtenerCitasPorDoctor(doctorId)
                if (citas.isEmpty()) {
                    Toast.makeText(this@VerCitasActivity, "No hay citas registradas", Toast.LENGTH_SHORT).show()
                } else {
                    mostrarCitas(citas)
                }
            } catch (e: Exception) {
                Toast.makeText(this@VerCitasActivity, "Error al cargar citas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarCitas(citas: List<CitaConDoctorDTO>) {
        recyclerCitas.removeAllViews() // Limpiar la vista antes de agregar nuevas citas

        for (cita in citas) {
            val citaView = layoutInflater.inflate(R.layout.item_cita, recyclerCitas, false)

            val txtFecha = citaView.findViewById<TextView>(R.id.txtFechaCita)
            val txtHora = citaView.findViewById<TextView>(R.id.txtHoraCita)
            val txtPaciente = citaView.findViewById<TextView>(R.id.txtPacienteCita)

            txtFecha.text = cita.fecha
            txtHora.text = cita.hora
            txtPaciente.text = "Paciente: ${cita.paciente_id}"
            recyclerCitas.addView(citaView) // Agregar la cita al LinearLayout
        }
    }
}
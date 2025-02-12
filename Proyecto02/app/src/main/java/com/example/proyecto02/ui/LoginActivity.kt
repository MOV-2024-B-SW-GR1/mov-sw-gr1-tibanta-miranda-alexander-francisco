package com.example.proyecto02.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto02.R
import com.example.proyecto02.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var editCedula: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistro: Button
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar la base de datos
        database = AppDatabase.getDatabase(this)

        // Vincular vistas
        editCedula = findViewById(R.id.editCedula)
        btnLogin = findViewById(R.id.btnIniciar)
        btnRegistro = findViewById(R.id.btnRegistro)

        // Eventos de los botones
        btnLogin.setOnClickListener { validarUsuario() }
        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun validarUsuario() {
        val cedula = editCedula.text.toString().trim()

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Ingrese su cédula", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val doctor = database.doctorDAO().obtenerDoctorPorCedula(cedula)
            val paciente = database.pacienteDAO().obtenerPacientePorCedula(cedula)

            launch(Dispatchers.Main) {
                when {
                    doctor != null -> {
                        Toast.makeText(applicationContext, "Bienvenido Doctor ${doctor.nombre}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, DoctorHomeActivity::class.java).apply {
                            putExtra("doctorId", doctor.cedula)
                            putExtra("nombreDoctor", doctor.nombre)
                        }
                        startActivity(intent)
                    }
                    paciente != null -> {
                        Toast.makeText(applicationContext, "Bienvenido ${paciente.nombre}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, PacienteHomeActivity::class.java).apply {
                            putExtra("pacienteId", paciente.cedula)
                            putExtra("nombrePaciente", paciente.nombre)
                        }
                        startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(applicationContext, "Cédula no registrada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
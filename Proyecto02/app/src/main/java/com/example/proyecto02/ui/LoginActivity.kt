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

        database = AppDatabase.getDatabase(this)

        editCedula = findViewById(R.id.editCedula)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistro = findViewById(R.id.btnRegistro)

        btnLogin.setOnClickListener {
            validarUsuario()
        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun validarUsuario() {
        val cedula = editCedula.text.toString()

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Ingrese su cédula", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val doctor = database.doctorDAO().obtenerDoctorPorCedula(cedula)
            val paciente = database.pacienteDAO().obtenerPacientePorCedula(cedula)

            runOnUiThread {
                when {
                    doctor != null -> {
                        Toast.makeText(applicationContext, "Bienvenido Doctor ${doctor.nombre}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, DoctorHomeActivity::class.java))
                    }
                    paciente != null -> {
                        Toast.makeText(applicationContext, "Bienvenido ${paciente.nombre}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, PacienteHomeActivity::class.java))
                    }
                    else -> {
                        Toast.makeText(applicationContext, "Cédula no registrada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
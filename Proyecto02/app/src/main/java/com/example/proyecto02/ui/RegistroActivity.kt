package com.example.proyecto02.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto02.R
import com.example.proyecto02.database.AppDatabase
import com.example.proyecto02.models.Doctor
import com.example.proyecto02.models.Paciente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    private lateinit var editCedula: EditText
    private lateinit var editNombre: EditText
    private lateinit var editApellido: EditText
    private lateinit var editEdad: EditText
    private lateinit var editPeso: EditText
    private lateinit var editAltura: EditText
    private lateinit var editDepartamento: EditText
    private lateinit var editEspecialidad: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnRegistrar: Button

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        database = AppDatabase.getDatabase(this)

        editCedula = findViewById(R.id.editCedula)
        editNombre = findViewById(R.id.editNombre)
        editApellido = findViewById(R.id.editApellido)
        editEdad = findViewById(R.id.editEdad)
        editPeso = findViewById(R.id.editPeso)
        editAltura = findViewById(R.id.editAltura)
        editDepartamento = findViewById(R.id.editDepartamento)
        editEspecialidad = findViewById(R.id.editEspecialidad)
        spinnerTipo = findViewById(R.id.spinnerTipo)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // Configurar el Spinner
        val opciones = arrayOf("Paciente", "Doctor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinnerTipo.adapter = adapter

        // Manejar la visibilidad de campos según el tipo seleccionado
        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (opciones[position] == "Doctor") {
                    editPeso.visibility = View.GONE
                    editAltura.visibility = View.GONE
                    editDepartamento.visibility = View.VISIBLE
                    editEspecialidad.visibility = View.VISIBLE
                } else {
                    editPeso.visibility = View.VISIBLE
                    editAltura.visibility = View.VISIBLE
                    editDepartamento.visibility = View.GONE
                    editEspecialidad.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val cedula = editCedula.text.toString()
        val nombre = editNombre.text.toString()
        val apellido = editApellido.text.toString()
        val edad = editEdad.text.toString().toIntOrNull() ?: 0

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edad <= 0) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val tipoUsuario = spinnerTipo.selectedItem.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            if (tipoUsuario == "Doctor") {
                val departamento = editDepartamento.text.toString()
                val especialidad = editEspecialidad.text.toString()
                if (departamento.isEmpty() || especialidad.isEmpty()) {
                    runOnUiThread { Toast.makeText(applicationContext, "Faltan datos", Toast.LENGTH_SHORT).show() }
                    return@launch
                }
                val doctor = Doctor(cedula, nombre, apellido, edad, departamento, especialidad)
                database.doctorDAO().insertarDoctor(doctor)
            } else {
                val peso = editPeso.text.toString().toDoubleOrNull() ?: 0.0
                val altura = editAltura.text.toString().toDoubleOrNull() ?: 0.0
                if (peso <= 0 || altura <= 0) {
                    runOnUiThread { Toast.makeText(applicationContext, "Peso y altura inválidos", Toast.LENGTH_SHORT).show() }
                    return@launch
                }
                val paciente = Paciente(cedula, nombre, apellido, edad, peso, altura)
                database.pacienteDAO().insertarPaciente(paciente)
            }

            runOnUiThread {
                Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
package com.example.deber2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpleadoDAO
import com.example.deber2.models.Empleado

class FormEmpleadoActivity : AppCompatActivity() {
    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtEdad: EditText
    private lateinit var txtDepartamento: EditText
    private lateinit var txtSalario: EditText
    private lateinit var btnGuardar: Button
    private lateinit var empleadoDAO: EmpleadoDAO
    private var empresaId: Int = -1
    private var empleadoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_empleado)

        empleadoDAO = EmpleadoDAO(this)
        txtNombre = findViewById(R.id.etNombreEmpleado)
        txtApellido = findViewById(R.id.etApellidoEmpleado)
        txtEdad = findViewById(R.id.etEdadEmpleado)
        txtDepartamento = findViewById(R.id.etDepartamentoEmpleado)
        txtSalario = findViewById(R.id.etSalarioEmpleado)
        btnGuardar = findViewById(R.id.btnGuardarEmpleado)

        empresaId = intent.getIntExtra("empresaId", -1)
        empleadoId = intent.getIntExtra("empleadoId", -1)

        // Si se recibió un empleadoId, cargar datos
        if (empleadoId != -1) {
            val empleado = empleadoDAO.obtenerEmpleadoPorId(empleadoId)
            empleado?.let {
                txtNombre.setText(it.nombre)
                txtApellido.setText(it.apellido)
                txtEdad.setText(it.edad.toString())
                txtDepartamento.setText(it.departamento)
                txtSalario.setText(it.salario.toString())
            }
        }

        btnGuardar.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val apellido = txtApellido.text.toString()
            val edad = txtEdad.text.toString().toIntOrNull() ?: 0
            val departamento = txtDepartamento.text.toString()
            val salario = txtSalario.text.toString().toDoubleOrNull() ?: 0.0

            if (empleadoId == -1) {
                // Nuevo empleado
                val nuevoEmpleado = Empleado(
                    id = 0, // ID autoincremental
                    nombre = nombre,
                    apellido = apellido,
                    edad = edad,
                    departamento = departamento,
                    salario = salario,
                    empresaId = empresaId
                )
                empleadoDAO.insertarEmpleado(nuevoEmpleado)
            } else {
                // Editar empleado existente
                val empleadoEditado = Empleado(
                    id = empleadoId,
                    nombre = nombre,
                    apellido = apellido,
                    edad = edad,
                    departamento = departamento,
                    salario = salario,
                    empresaId = empresaId
                )
                empleadoDAO.actualizarEmpleado(empleadoId, empleadoEditado)
            }
            // Redirigir a EmpleadosActivity después de guardar
            val intent = Intent(this, EmpleadosActivity::class.java)
            intent.putExtra("empresaId", empresaId) // Para que EmpleadosActivity cargue los empleados de la empresa
            startActivity(intent)
            finish()
        }
    }
}
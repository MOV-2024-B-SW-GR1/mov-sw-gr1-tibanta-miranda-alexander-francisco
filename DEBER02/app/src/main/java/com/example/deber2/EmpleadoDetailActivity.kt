package com.example.deber2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpleadoDAO
import com.example.deber2.models.Empleado

class EmpleadoDetailActivity : AppCompatActivity() {
    private lateinit var txtEmpleadoNombre: TextView
    private lateinit var btnEditarEmpleado: Button
    private lateinit var btnEliminarEmpleado: Button
    private lateinit var empleadoDAO: EmpleadoDAO

    private var empleadoId: Int = -1
    private lateinit var empleado: Empleado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleado_detail)

        txtEmpleadoNombre = findViewById(R.id.txtEmpleadoNombre)
        btnEditarEmpleado = findViewById(R.id.btnEditarEmpleado)
        btnEliminarEmpleado = findViewById(R.id.btnEliminarEmpleado)

        empleadoDAO = EmpleadoDAO(this)
        empleadoId = intent.getIntExtra("empleadoId", -1)

        if (empleadoId != -1) {
            empleado = empleadoDAO.obtenerEmpleadoPorId(empleadoId) ?: return
            txtEmpleadoNombre.text = "${empleado.nombre} ${empleado.apellido}"
        }

        // Editar empleado
        btnEditarEmpleado.setOnClickListener {
            val intent = Intent(this, FormEmpleadoActivity::class.java)
            intent.putExtra("empleadoId", empleadoId)
            intent.putExtra("empresaId", empleado.empresaId)
            startActivity(intent)
        }

        // Eliminar empleado
        btnEliminarEmpleado.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Empleado")
                .setMessage("¿Estás seguro de eliminar a ${empleado.nombre} ${empleado.apellido}?")
                .setPositiveButton("Sí") { _, _ ->
                    empleadoDAO.eliminarEmpleado(empleadoId)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
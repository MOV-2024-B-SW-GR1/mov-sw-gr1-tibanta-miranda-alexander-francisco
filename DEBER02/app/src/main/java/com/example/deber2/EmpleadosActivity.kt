package com.example.deber2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpleadoDAO
import com.example.deber2.models.Empleado

class EmpleadosActivity : AppCompatActivity() {
    private lateinit var listViewEmpleados: ListView
    private lateinit var btnAgregarEmpleado: Button
    private lateinit var btnSalir: Button
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var empleadoDAO: EmpleadoDAO
    private var empleados = mutableListOf<Empleado>()
    private var empresaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleados)

        empresaId = intent.getIntExtra("empresaId", -1)
        empleadoDAO = EmpleadoDAO(this)
        listViewEmpleados = findViewById(R.id.listViewEmpleados)
        btnAgregarEmpleado = findViewById(R.id.btnAgregarEmpleado)
        btnSalir = findViewById(R.id.btnSalir)

        cargarEmpleados()

        btnAgregarEmpleado.setOnClickListener {
            val intent = Intent(this, FormEmpleadoActivity::class.java)
            intent.putExtra("empresaId", empresaId)
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Cierra actividades previas
            startActivity(intent)
            finish() // Cierra EmpleadosActivity
        }

        listViewEmpleados.setOnItemClickListener { _, _, position, _ ->
            val empleadoSeleccionado = empleados[position]
            val intent = Intent(this, EmpleadoDetailActivity::class.java)
            intent.putExtra("empleadoId", empleadoSeleccionado.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEmpleados()
    }

    private fun cargarEmpleados() {
        empleados = empleadoDAO.obtenerEmpleadosPorEmpresa(empresaId).toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, empleados.map { "${it.nombre} ${it.apellido}" })
        listViewEmpleados.adapter = adapter
    }
}
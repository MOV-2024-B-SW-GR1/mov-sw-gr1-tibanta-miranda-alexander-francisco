package com.example.deber1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empleado
import com.example.deber1.models.Empresa

class EmpleadosActivity : AppCompatActivity() {
    private lateinit var listViewEmpleados: ListView
    private lateinit var btnAgregarEmpleado: Button
    private lateinit var btnSalir: Button
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var empresa: Empresa
    private var empresaIndex: Int = -1
    private val empleados = mutableListOf<Empleado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleados)

        listViewEmpleados = findViewById(R.id.listViewEmpleados)
        btnAgregarEmpleado = findViewById(R.id.btnAgregarEmpleado)
        btnSalir = findViewById(R.id.btnSalir)

        empresa = intent.getParcelableExtra("empresa")!!
        empresaIndex = intent.getIntExtra("empresaIndex", -1)
        empleados.addAll(empresa.empleados)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, empleados.map { it.nombre }.toMutableList())
        listViewEmpleados.adapter = adapter

        btnAgregarEmpleado.setOnClickListener {
            val intent = Intent(this, FormEmpleadoActivity::class.java)
            intent.putExtra("empresaIndex", empresaIndex)
            startActivityForResult(intent, 5)  // Código de solicitud 5 para agregar
        }

        listViewEmpleados.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, EmpleadoDetailActivity::class.java)
            intent.putExtra("empleado", empleados[position])
            intent.putExtra("empleadoIndex", position)
            startActivityForResult(intent, 6)  // Código de solicitud 6 para editar/eliminar
        }

        btnSalir.setOnClickListener {
            val intent = Intent()
            empresa.empleados = empleados
            intent.putExtra("empresa", empresa)
            intent.putExtra("empresaIndex", empresaIndex)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                5 -> { // Agregar Empleado
                    val empleado = data?.getParcelableExtra<Empleado>("empleado")
                    empleado?.let {
                        empleados.add(it)
                        actualizarLista()
                    }
                }
                6, 7 -> { // Editar o Eliminar Empleado
                    val empleado = data?.getParcelableExtra<Empleado>("empleado")
                    val index = data?.getIntExtra("empleadoIndex", -1) ?: -1
                    val eliminar = data?.getBooleanExtra("eliminar", false) ?: false

                    if (index != -1) {
                        if (eliminar) {
                            empleados.removeAt(index)
                        } else {
                            empleado?.let { empleados[index] = it }
                        }
                        actualizarLista()
                    }
                }
            }
        }
    }

    private fun actualizarLista() {
        adapter.clear()
        adapter.addAll(empleados.map { it.nombre })
        adapter.notifyDataSetChanged()
    }
}
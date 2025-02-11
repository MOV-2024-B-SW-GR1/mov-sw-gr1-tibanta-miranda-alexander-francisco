package com.example.deber1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empleado

class EmpleadoDetailActivity : AppCompatActivity() {
    private lateinit var txtEmpleadoNombre: TextView
    private lateinit var btnEditarEmpleado: Button
    private lateinit var btnEliminarEmpleado: Button

    private var empresaIndex: Int = -1
    private var empleadoIndex: Int = -1
    private lateinit var empleado: Empleado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleado_detail)

        txtEmpleadoNombre = findViewById(R.id.txtEmpleadoNombre)
        btnEditarEmpleado = findViewById(R.id.btnEditarEmpleado)
        btnEliminarEmpleado = findViewById(R.id.btnEliminarEmpleado)

        empresaIndex = intent.getIntExtra("empresaIndex", -1)
        empleadoIndex = intent.getIntExtra("empleadoIndex", -1)
        empleado = intent.getParcelableExtra("empleado")!!

        txtEmpleadoNombre.text = "${empleado.nombre} ${empleado.apellido}"

        // Botón para editar empleado
        btnEditarEmpleado.setOnClickListener {
            val intent = Intent(this, FormEmpleadoActivity::class.java)
            intent.putExtra("empresaIndex", empresaIndex)
            intent.putExtra("empleadoIndex", empleadoIndex)
            intent.putExtra("nombre", empleado.nombre)
            intent.putExtra("apellido", empleado.apellido)
            intent.putExtra("edad", empleado.edad)
            intent.putExtra("departamento", empleado.departamento)
            intent.putExtra("salario", empleado.salario)
            startActivityForResult(intent, 7) // Código 7 para editar empleado
        }

        // Botón para eliminar empleado
        btnEliminarEmpleado.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("empleadoIndex", empleadoIndex)
            resultIntent.putExtra("eliminar", true)  // Indicador de eliminación
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val empleadoActualizado = data?.getParcelableExtra<Empleado>("empleado")
            val resultIntent = Intent()
            resultIntent.putExtra("empleado", empleadoActualizado)
            resultIntent.putExtra("empleadoIndex", empleadoIndex)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
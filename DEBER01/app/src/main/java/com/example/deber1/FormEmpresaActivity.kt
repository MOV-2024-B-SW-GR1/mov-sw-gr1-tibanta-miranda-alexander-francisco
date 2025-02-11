package com.example.deber1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empresa

class FormEmpresaActivity : AppCompatActivity() {
    private lateinit var txtNombreEmpresa: EditText
    private lateinit var txtCantidadEmpleados: EditText
    private lateinit var txtDireccionEmpresa: EditText
    private lateinit var btnGuardarEmpresa: Button
    private var empresaIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_empresa)

        txtNombreEmpresa = findViewById(R.id.etNombreEmpresa)
        txtCantidadEmpleados = findViewById(R.id.etCantidadEmpleados)
        txtDireccionEmpresa = findViewById(R.id.etDireccionEmpresa)
        btnGuardarEmpresa = findViewById(R.id.btnGuardarEmpresa)

        val empresa = intent.getParcelableExtra<Empresa>("empresa")
        empresaIndex = intent.getIntExtra("empresaIndex", -1)

        empresa?.let {
            txtNombreEmpresa.setText(it.nombre)
            txtCantidadEmpleados.setText(it.cantidadEmpleados.toString())
            txtDireccionEmpresa.setText(it.direccion)
        }

        btnGuardarEmpresa.setOnClickListener {
            val nombre = txtNombreEmpresa.text.toString()
            val cantidadEmpleados = txtCantidadEmpleados.text.toString().toIntOrNull() ?: 0
            val direccion = txtDireccionEmpresa.text.toString()

            val empresaEditada = Empresa(nombre, cantidadEmpleados, direccion)

            val intent = Intent()
            intent.putExtra("empresa", empresaEditada)
            intent.putExtra("empresaIndex", empresaIndex)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
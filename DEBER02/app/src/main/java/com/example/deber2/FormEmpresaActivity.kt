package com.example.deber2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpresaDAO
import com.example.deber2.models.Empresa

class FormEmpresaActivity : AppCompatActivity() {
    private lateinit var txtNombreEmpresa: EditText
    private lateinit var txtCantidadEmpleados: EditText
    private lateinit var txtDireccionEmpresa: EditText
    private lateinit var btnGuardarEmpresa: Button
    private lateinit var empresaDAO: EmpresaDAO
    private var empresaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_empresa)

        empresaDAO = EmpresaDAO(this)
        txtNombreEmpresa = findViewById(R.id.etNombreEmpresa)
        txtCantidadEmpleados = findViewById(R.id.etCantidadEmpleados)
        txtDireccionEmpresa = findViewById(R.id.etDireccionEmpresa)
        btnGuardarEmpresa = findViewById(R.id.btnGuardarEmpresa)

        empresaId = intent.getIntExtra("empresaId", -1)

        if (empresaId != -1) {
            val empresa = empresaDAO.obtenerEmpresaPorId(empresaId)
            empresa?.let {
                txtNombreEmpresa.setText(it.nombre)
                txtCantidadEmpleados.setText(it.cantidadEmpleados)
                txtDireccionEmpresa.setText(it.direccion)
            }
        }

        btnGuardarEmpresa.setOnClickListener {
            val nombre = txtNombreEmpresa.text.toString()
            val cantidadEmpleados = txtCantidadEmpleados.text.toString().toIntOrNull() ?: 0
            val direccion = txtDireccionEmpresa.text.toString()

            if (empresaId == -1) {
                // Nueva empresa (ID será autoincremental en la BD)
                val nuevaEmpresa = Empresa(id = 0.toString(), nombre = nombre, cantidadEmpleados = cantidadEmpleados.toString(), direccion = direccion)
                empresaDAO.insertarEmpresa(nuevaEmpresa)
            } else {
                // Editar empresa existente
                val empresaEditada = Empresa(id = empresaId.toString(), nombre = nombre, cantidadEmpleados = cantidadEmpleados.toString(), direccion = direccion)
                empresaDAO.actualizarEmpresa(empresaId, empresaEditada)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.example.deber1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empresa

class EmpresaDetailActivity : AppCompatActivity() {
    private lateinit var txtEmpresaNombre: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnVerEmpleados: Button
    private lateinit var btnSalir: Button
    private lateinit var empresa: Empresa
    private var empresaIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_detail)

        txtEmpresaNombre = findViewById(R.id.txtEmpresaNombre)
        btnEditar = findViewById(R.id.btnEditarEmpresa)
        btnEliminar = findViewById(R.id.btnEliminarEmpresa)
        btnVerEmpleados = findViewById(R.id.btnVerEmpleados)
        btnSalir = findViewById(R.id.btnSalirEmpresa)

        empresa = intent.getParcelableExtra("empresa")!!
        empresaIndex = intent.getIntExtra("empresaIndex", -1)

        txtEmpresaNombre.text = empresa.nombre

        btnEditar.setOnClickListener {
            val intent = Intent(this, FormEmpresaActivity::class.java)
            intent.putExtra("empresa", empresa)
            intent.putExtra("empresaIndex", empresaIndex)
            startActivityForResult(intent, 3)
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Empresa")
                .setMessage("¿Estás seguro de eliminar ${empresa.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent()
                    intent.putExtra("action", "delete")
                    intent.putExtra("empresaIndex", empresaIndex)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        btnVerEmpleados.setOnClickListener {
            val intent = Intent(this, EmpleadosActivity::class.java)
            intent.putExtra("empresa", empresa)
            intent.putExtra("empresaIndex", empresaIndex)
            startActivityForResult(intent, 4)
        }

        // Acción del botón Salir
        btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            val empresaEditada = data.getParcelableExtra<Empresa>("empresa")
            if (empresaEditada != null) {
                val intent = Intent()
                intent.putExtra("action", "edit")
                intent.putExtra("empresa", empresaEditada)
                intent.putExtra("empresaIndex", empresaIndex)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}
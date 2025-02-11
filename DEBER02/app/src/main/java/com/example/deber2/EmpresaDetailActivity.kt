package com.example.deber2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpresaDAO
import com.example.deber2.models.Empresa

class EmpresaDetailActivity : AppCompatActivity() {
    private lateinit var txtEmpresaNombre: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnVerEmpleados: Button
    private lateinit var btnSalir: Button
    private lateinit var empresaDAO: EmpresaDAO
    private var empresaId: Int = -1
    private lateinit var empresa: Empresa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_detail)

        empresaDAO = EmpresaDAO(this)
        txtEmpresaNombre = findViewById(R.id.txtEmpresaNombre)
        btnEditar = findViewById(R.id.btnEditarEmpresa)
        btnEliminar = findViewById(R.id.btnEliminarEmpresa)
        btnVerEmpleados = findViewById(R.id.btnVerEmpleados)
        btnSalir = findViewById(R.id.btnSalirEmpresa)

        Log.d("EmpresaDetailActivity", "empresaId recibido: $empresaId")
        empresaId = intent.getStringExtra("empresaId")?.toIntOrNull() ?: -1
        empresa = empresaDAO.obtenerEmpresaPorId(empresaId) ?: return

        if (empresaId == -1) {
            Toast.makeText(this, "Error al obtener empresa", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        txtEmpresaNombre.text = empresa.nombre

        btnEditar.setOnClickListener {
            val intent = Intent(this, FormEmpresaActivity::class.java)
            intent.putExtra("empresaId", empresa.id.toInt())  // Asegurar que el ID se pase correctamente
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Empresa")
                .setMessage("¿Estás seguro de eliminar ${empresa.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    empresaDAO.eliminarEmpresa(empresaId)
                    setResult(RESULT_OK)  // Indicar que hubo cambios
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        btnVerEmpleados.setOnClickListener {
            val intent = Intent(this, EmpleadosActivity::class.java)
            intent.putExtra("empresaId", empresa.id.toInt())  // Convertir correctamente
            startActivity(intent)
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
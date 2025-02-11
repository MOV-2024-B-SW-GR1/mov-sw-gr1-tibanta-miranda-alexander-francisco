package com.example.deber2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber2.database.EmpresaDAO
import com.example.deber2.models.Empresa

class MainActivity : AppCompatActivity() {
    private lateinit var listViewEmpresas: ListView
    private lateinit var btnAgregarEmpresa: Button
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var empresaDAO: EmpresaDAO
    private var empresas = mutableListOf<Empresa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        empresaDAO = EmpresaDAO(this)
        listViewEmpresas = findViewById(R.id.listViewEmpresas)
        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa)

        cargarEmpresas()

        btnAgregarEmpresa.setOnClickListener {
            startActivity(Intent(this, FormEmpresaActivity::class.java))
        }

        listViewEmpresas.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, EmpresaDetailActivity::class.java)
            intent.putExtra("empresaId", empresas[position].id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEmpresas()
    }

    private fun cargarEmpresas() {
        empresas = empresaDAO.obtenerEmpresas().toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, empresas.map { it.nombre })
        listViewEmpresas.adapter = adapter
    }
}
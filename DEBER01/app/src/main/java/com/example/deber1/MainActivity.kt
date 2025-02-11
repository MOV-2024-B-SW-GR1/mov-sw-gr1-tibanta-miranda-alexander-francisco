package com.example.deber1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empresa

class MainActivity : AppCompatActivity() {
    private lateinit var listViewEmpresas: ListView
    private lateinit var btnAgregarEmpresa: Button
    private lateinit var adapter: ArrayAdapter<String>

    // Lista de empresas
    val empresas = mutableListOf<Empresa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewEmpresas = findViewById(R.id.listViewEmpresas)
        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa)

        // Inicializar adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, empresas.map { it.nombre }.toMutableList())
        listViewEmpresas.adapter = adapter

        btnAgregarEmpresa.setOnClickListener {
            val intent = Intent(this, FormEmpresaActivity::class.java)
            startActivityForResult(intent, 1)
        }

        listViewEmpresas.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, EmpresaDetailActivity::class.java)
            intent.putExtra("empresa", empresas[position])
            intent.putExtra("empresaIndex", position)
            startActivityForResult(intent, 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                1 -> { // Nueva empresa
                    val empresa = data.getParcelableExtra<Empresa>("empresa")
                    empresa?.let {
                        empresas.add(it)
                        actualizarLista()
                    }
                }
                2 -> { // Editar o eliminar empresa
                    val action = data.getStringExtra("action")
                    val empresaIndex = data.getIntExtra("empresaIndex", -1)

                    when (action) {
                        "edit" -> {
                            val empresaEditada = data.getParcelableExtra<Empresa>("empresa")
                            if (empresaIndex != -1 && empresaEditada != null) {
                                empresas[empresaIndex] = empresaEditada
                                actualizarLista()
                            }
                        }
                        "delete" -> {
                            if (empresaIndex != -1) {
                                empresas.removeAt(empresaIndex)
                                actualizarLista()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun actualizarLista() {
        adapter.clear()
        adapter.addAll(empresas.map { it.nombre })
        adapter.notifyDataSetChanged()
    }
}
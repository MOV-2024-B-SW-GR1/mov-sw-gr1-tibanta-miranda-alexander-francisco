package com.example.deber1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.models.Empleado

class FormEmpleadoActivity : AppCompatActivity() {
    private lateinit var txtNombreEmpleado: EditText
    private lateinit var txtApellidoEmpleado: EditText
    private lateinit var txtEdadEmpleado: EditText
    private lateinit var txtDepartamentoEmpleado: EditText
    private lateinit var txtSalarioEmpleado: EditText
    private lateinit var btnGuardarEmpleado: Button

    private var empresaIndex: Int = -1
    private var empleadoIndex: Int = -1
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_empleado)

        txtNombreEmpleado = findViewById(R.id.etNombreEmpleado)
        txtApellidoEmpleado = findViewById(R.id.etApellidoEmpleado)
        txtEdadEmpleado = findViewById(R.id.etEdadEmpleado)
        txtDepartamentoEmpleado = findViewById(R.id.etDepartamentoEmpleado)
        txtSalarioEmpleado = findViewById(R.id.etSalarioEmpleado)
        btnGuardarEmpleado = findViewById(R.id.btnGuardarEmpleado)

        empresaIndex = intent.getIntExtra("empresaIndex", -1)
        empleadoIndex = intent.getIntExtra("empleadoIndex", -1)

        if (empleadoIndex != -1) {
            isEditing = true
            txtNombreEmpleado.setText(intent.getStringExtra("nombre"))
            txtApellidoEmpleado.setText(intent.getStringExtra("apellido"))
            txtEdadEmpleado.setText(intent.getIntExtra("edad", 0).toString())
            txtDepartamentoEmpleado.setText(intent.getStringExtra("departamento"))
            txtSalarioEmpleado.setText(intent.getDoubleExtra("salario", 0.0).toString())
        }

        btnGuardarEmpleado.setOnClickListener {
            val empleado = Empleado(
                txtNombreEmpleado.text.toString(),
                txtApellidoEmpleado.text.toString(),
                txtEdadEmpleado.text.toString().toInt(),
                txtDepartamentoEmpleado.text.toString(),
                txtSalarioEmpleado.text.toString().toDouble()
            )

            val resultIntent = Intent()
            resultIntent.putExtra("empleado", empleado)
            resultIntent.putExtra("empleadoIndex", empleadoIndex)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
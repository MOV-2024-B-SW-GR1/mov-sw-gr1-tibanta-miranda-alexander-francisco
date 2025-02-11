package com.example.deber2.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.deber2.models.Empleado

class EmpleadoDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertarEmpleado(empleado: Empleado): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_EMPLEADO_NOMBRE, empleado.nombre)
            put(DatabaseHelper.COLUMN_EMPLEADO_APELLIDO, empleado.apellido)
            put(DatabaseHelper.COLUMN_EMPLEADO_EDAD, empleado.edad)
            put(DatabaseHelper.COLUMN_EMPLEADO_DEPARTAMENTO, empleado.departamento)
            put(DatabaseHelper.COLUMN_EMPLEADO_SALARIO, empleado.salario)
            put(DatabaseHelper.COLUMN_EMPLEADO_EMPRESA_ID, empleado.empresaId)
        }
        val id = db.insert(DatabaseHelper.TABLE_EMPLEADO, null, values)
        db.close()
        return id
    }

    fun obtenerEmpleadosPorEmpresa(empresaId: Int): List<Empleado> {
        val empleados = mutableListOf<Empleado>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_EMPLEADO} WHERE ${DatabaseHelper.COLUMN_EMPLEADO_EMPRESA_ID} = ?",
            arrayOf(empresaId.toString())
        )

        while (cursor.moveToNext()) {
            val empleado = Empleado(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_NOMBRE)),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_APELLIDO)),
                edad = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_EDAD)),
                departamento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_DEPARTAMENTO)),
                salario = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_SALARIO)),
                empresaId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_EMPRESA_ID))
            )
            empleados.add(empleado)
        }
        cursor.close()
        db.close()
        return empleados
    }

    fun obtenerEmpleadoPorId(id: Int): Empleado? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_EMPLEADO} WHERE ${DatabaseHelper.COLUMN_EMPLEADO_ID} = ?",
            arrayOf(id.toString())
        )

        return if (cursor.moveToFirst()) {
            Empleado(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_NOMBRE)),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_APELLIDO)),
                edad = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_EDAD)),
                departamento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_DEPARTAMENTO)),
                salario = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_SALARIO)),
                empresaId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLEADO_EMPRESA_ID))
            )
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }

    fun actualizarEmpleado(id: Int, empleado: Empleado): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_EMPLEADO_NOMBRE, empleado.nombre)
            put(DatabaseHelper.COLUMN_EMPLEADO_APELLIDO, empleado.apellido)
            put(DatabaseHelper.COLUMN_EMPLEADO_EDAD, empleado.edad)
            put(DatabaseHelper.COLUMN_EMPLEADO_DEPARTAMENTO, empleado.departamento)
            put(DatabaseHelper.COLUMN_EMPLEADO_SALARIO, empleado.salario)
            put(DatabaseHelper.COLUMN_EMPLEADO_EMPRESA_ID, empleado.empresaId)
        }
        val result = db.update(
            DatabaseHelper.TABLE_EMPLEADO,
            values,
            "${DatabaseHelper.COLUMN_EMPLEADO_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }

    fun eliminarEmpleado(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_EMPLEADO, "${DatabaseHelper.COLUMN_EMPLEADO_ID}=?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
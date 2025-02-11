package com.example.deber2.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.deber2.models.Empresa

class EmpresaDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertarEmpresa(empresa: Empresa): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_EMPRESA_NOMBRE, empresa.nombre)
            put(DatabaseHelper.COLUMN_EMPRESA_CANTIDAD, empresa.cantidadEmpleados)
            put(DatabaseHelper.COLUMN_EMPRESA_DIRECCION, empresa.direccion)
        }
        val id = db.insert(DatabaseHelper.TABLE_EMPRESA, null, values)
        db.close()
        return id
    }

    fun obtenerEmpresas(): List<Empresa> {
        val empresas = mutableListOf<Empresa>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_EMPRESA}", null)

        while (cursor.moveToNext()) {
            val empresa = Empresa(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_ID)).toString(),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_NOMBRE)),
                cantidadEmpleados = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_CANTIDAD))
                    .toString(),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_DIRECCION))
            )
            empresas.add(empresa)
        }
        cursor.close()
        db.close()
        return empresas
    }

    fun eliminarEmpresa(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_EMPRESA, "${DatabaseHelper.COLUMN_EMPRESA_ID}=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun obtenerEmpresaPorId(id: Int): Empresa? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_EMPRESA} WHERE ${DatabaseHelper.COLUMN_EMPRESA_ID} = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            Empresa(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_ID)).toString(),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_NOMBRE)),
                cantidadEmpleados = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_CANTIDAD))
                    .toString(),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPRESA_DIRECCION))
            )
        } else {
            null
        }.also {
            cursor.close()
            db.close()
        }
    }

    fun actualizarEmpresa(id: Int, empresa: Empresa): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_EMPRESA_NOMBRE, empresa.nombre)
            put(DatabaseHelper.COLUMN_EMPRESA_CANTIDAD, empresa.cantidadEmpleados)
            put(DatabaseHelper.COLUMN_EMPRESA_DIRECCION, empresa.direccion)
        }
        val result = db.update(DatabaseHelper.TABLE_EMPRESA, values, "${DatabaseHelper.COLUMN_EMPRESA_ID}=?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
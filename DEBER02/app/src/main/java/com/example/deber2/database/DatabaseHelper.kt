package com.example.deber2.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_EMPRESA)
        db.execSQL(CREATE_TABLE_EMPLEADO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPRESA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPLEADO")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "empresa.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_EMPRESA = "empresa"
        const val COLUMN_EMPRESA_ID = "id"
        const val COLUMN_EMPRESA_NOMBRE = "nombre"
        const val COLUMN_EMPRESA_CANTIDAD = "cantidadEmpleados"
        const val COLUMN_EMPRESA_DIRECCION = "direccion"

        const val TABLE_EMPLEADO = "empleado"
        const val COLUMN_EMPLEADO_ID = "id"
        const val COLUMN_EMPLEADO_NOMBRE = "nombre"
        const val COLUMN_EMPLEADO_APELLIDO = "apellido"
        const val COLUMN_EMPLEADO_EDAD = "edad"
        const val COLUMN_EMPLEADO_DEPARTAMENTO = "departamento"
        const val COLUMN_EMPLEADO_SALARIO = "salario"
        const val COLUMN_EMPLEADO_EMPRESA_ID = "empresa_id"

        private const val CREATE_TABLE_EMPRESA = """
            CREATE TABLE $TABLE_EMPRESA (
                $COLUMN_EMPRESA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMPRESA_NOMBRE TEXT NOT NULL,
                $COLUMN_EMPRESA_CANTIDAD INTEGER NOT NULL,
                $COLUMN_EMPRESA_DIRECCION TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_EMPLEADO = """
            CREATE TABLE $TABLE_EMPLEADO (
                $COLUMN_EMPLEADO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMPLEADO_NOMBRE TEXT NOT NULL,
                $COLUMN_EMPLEADO_APELLIDO TEXT NOT NULL,
                $COLUMN_EMPLEADO_EDAD INTEGER NOT NULL,
                $COLUMN_EMPLEADO_DEPARTAMENTO TEXT NOT NULL,
                $COLUMN_EMPLEADO_SALARIO REAL NOT NULL,
                $COLUMN_EMPLEADO_EMPRESA_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_EMPLEADO_EMPRESA_ID) REFERENCES $TABLE_EMPRESA($COLUMN_EMPRESA_ID) ON DELETE CASCADE
            )
        """
    }
}
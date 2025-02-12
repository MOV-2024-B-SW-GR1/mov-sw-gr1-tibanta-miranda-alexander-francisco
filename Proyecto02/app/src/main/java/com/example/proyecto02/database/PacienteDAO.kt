package com.example.proyecto02.database

import androidx.room.*
import com.example.proyecto02.models.Paciente

@Dao
interface PacienteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPaciente(paciente: Paciente)

    @Query("SELECT * FROM pacientes WHERE cedula = :cedula LIMIT 1")
    suspend fun obtenerPacientePorCedula(cedula: String): Paciente?

    @Update
    suspend fun actualizarPaciente(paciente: Paciente)

    @Delete
    suspend fun eliminarPaciente(paciente: Paciente)
}
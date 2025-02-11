package com.example.proyecto02.database

import androidx.room.*
import com.example.proyecto02.models.Doctor

@Dao
interface DoctorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDoctor(doctor: Doctor)

    @Query("SELECT * FROM doctores WHERE especialidad = :especialidad")
    suspend fun obtenerDoctoresPorEspecialidad(especialidad: String): List<Doctor>

    @Query("SELECT * FROM doctores WHERE cedula = :cedula LIMIT 1")
    suspend fun obtenerDoctorPorCedula(cedula: String): Doctor?

    @Query("SELECT * FROM doctores WHERE nombre LIKE :nombre || '%' LIMIT 1")
    suspend fun obtenerDoctorPorNombre(nombre: String): Doctor?

    @Query("SELECT * FROM doctores")
    suspend fun obtenerTodosLosDoctores(): List<Doctor>

    @Update
    suspend fun actualizarDoctor(doctor: Doctor)

    @Delete
    suspend fun eliminarDoctor(doctor: Doctor)
}
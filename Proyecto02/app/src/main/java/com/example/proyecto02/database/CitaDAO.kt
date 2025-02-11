package com.example.proyecto02.database

import androidx.room.*
import com.example.proyecto02.models.Cita
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.proyecto02.models.CitaConDoctorDTO


@Dao
interface CitaDAO {
    @Insert
    suspend fun insertarCita(cita: Cita)

    @Query("""
        SELECT citas.id, citas.fecha, citas.hora, citas.paciente_id, citas.doctor_id, 
               doctores.nombre AS nombreDoctor, doctores.apellido AS apellidoDoctor 
        FROM citas
        INNER JOIN doctores ON citas.doctor_id = doctores.cedula
        WHERE citas.doctor_id = :doctorId
    """)
    suspend fun obtenerCitasPorDoctor(doctorId: String): List<CitaConDoctorDTO>

    @Query("SELECT * FROM citas WHERE paciente_id = :pacienteId")
    suspend fun obtenerCitasPorPaciente(pacienteId: String): List<Cita>

    @Query("SELECT * FROM citas WHERE fecha = :fecha")
    suspend fun obtenerCitasPorFecha(fecha: String): List<Cita>

    @Query("SELECT * FROM citas WHERE id = :citaId LIMIT 1")
    suspend fun obtenerCitaPorId(citaId: Int): Cita?

    @Update
    suspend fun actualizarCita(cita: Cita)

    @Query("DELETE FROM citas WHERE paciente_id = :pacienteId AND fecha = :fecha AND hora = :hora")
    suspend fun eliminarCita(pacienteId: String, fecha: String, hora: String)
}
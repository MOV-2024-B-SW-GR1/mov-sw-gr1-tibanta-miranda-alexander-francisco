package com.example.proyecto02.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyecto02.models.Cita
import com.example.proyecto02.models.Doctor
import com.example.proyecto02.models.Paciente

@Database(entities = [Doctor::class, Paciente::class, Cita::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun doctorDAO(): DoctorDAO
    abstract fun pacienteDAO(): PacienteDAO
    abstract fun citaDAO(): CitaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fisioterapia_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
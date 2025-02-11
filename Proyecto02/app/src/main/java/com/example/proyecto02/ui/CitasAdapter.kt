package com.example.proyecto02.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto02.R
import com.example.proyecto02.models.Cita

class CitasAdapter(private val citas: List<Cita>) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    // ViewHolder para enlazar cada vista con los datos
    class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDoctorId: TextView = itemView.findViewById(R.id.txtDoctorId)
        val txtPacienteId: TextView = itemView.findViewById(R.id.txtPacienteId)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtHora: TextView = itemView.findViewById(R.id.txtHora)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        // Inflar el layout de cada item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_citas_adapter, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        // Obtener la cita correspondiente a la posición y mostrar sus datos
        val cita = citas[position]
        holder.txtDoctorId.text = cita.doctor_id
        holder.txtPacienteId.text = cita.paciente_id
        holder.txtFecha.text = cita.fecha
        holder.txtHora.text = cita.hora
    }

    override fun getItemCount(): Int {
        return citas.size
    }
}
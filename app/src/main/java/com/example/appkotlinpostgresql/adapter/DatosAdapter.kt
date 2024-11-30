package com.example.appkotlinpostgresql.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlinpostgresql.R

class DatosAdapter(private val datosList: List<Map<String, Any>>) :
    RecyclerView.Adapter<DatosAdapter.DatosViewHolder>() {

    // ViewHolder: Define las vistas para cada elemento
    class DatosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCompleto: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_datos, parent, false)
        return DatosViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatosViewHolder, position: Int) {
        val datos = datosList[position]

        // Combinar nombres y apellidos
        val nombreCompleto = "${datos["nombres"]?.toString() ?: ""} ${datos["primer_apellido"]?.toString() ?: ""} ${datos["segundo_apellido"]?.toString() ?: ""}"
        holder.tvNombreCompleto.text = nombreCompleto

        // Mostrar la edad con un valor predeterminado
        val edad = datos["edad"]?.toString() ?: "Sin edad"
        holder.tvEdad.text = holder.itemView.context.getString(R.string.edad_text, edad)
    }

    override fun getItemCount(): Int {
        return datosList.size
    }
}

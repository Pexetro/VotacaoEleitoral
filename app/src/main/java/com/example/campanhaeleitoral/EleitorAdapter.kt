package com.example.campanhaeleitoral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EleitorAdapter(
    private val entrevistados: List<Entrevistado>
) : RecyclerView.Adapter<EleitorAdapter.EleitorViewHolder>() {

    class EleitorViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvCelular: TextView = itemView.findViewById(R.id.tvCelular)
        val tvCidade: TextView = itemView.findViewById(R.id.tvCidade)
        val tvDataHora: TextView = itemView.findViewById(R.id.tvDataHora)
        val tvLatitude: TextView = itemView.findViewById(R.id.tvLatitude)
        val tvLongitude: TextView = itemView.findViewById(R.id.tvLongitude)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EleitorViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrevistado, parent, false)

        return EleitorViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EleitorViewHolder,
        position: Int
    ) {

        val entrevistado = entrevistados[position]

        holder.tvNome.text = entrevistado.nome
        holder.tvCelular.text = "Celular: ${entrevistado.telefone}"
        holder.tvCidade.text = "Cidade: ${entrevistado.cidade}"

        val formato = SimpleDateFormat(
            "dd/MM/yyyy 'às' HH:mm",
            Locale.getDefault()
        )

        val dataFormatada = formato.format(
            Date(entrevistado.dataHora)
        )

        holder.tvDataHora.text = "Data e hora: $dataFormatada"

        holder.tvLatitude.text =
            "Latitude: ${entrevistado.latitude ?: "Não disponível"}"

        holder.tvLongitude.text =
            "Longitude: ${entrevistado.longitude ?: "Não disponível"}"
    }

    override fun getItemCount(): Int {
        return entrevistados.size
    }
}

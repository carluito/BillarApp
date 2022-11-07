package com.billarapp.partida.verPartidas.pendientes

import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewItemPartidaBinding
import com.billarapp.partida.verPartidas.Partida


class PendientesHolder(private val binding: ViewItemPartidaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(partida: Partida, pendientesClickListener: (Partida)->Unit){

        binding.tvPartidaProvincia.text = partida.Provincia
        binding.tvPartidaLocalidad.text= partida.Localidad
        binding.tvPartidaLocal.text=partida.Local
        binding.tvPartidaFecha.text= partida.Fecha
        binding.tvPartidaHora.text=partida.Hora
        binding.tvPublicadoX2.text=partida.Jugador
        binding.tvPartidaNivel.text=partida.Nivel

        itemView.setOnClickListener {
            pendientesClickListener(partida)

            return@setOnClickListener
        }
    }
}
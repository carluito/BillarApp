package com.billarapp.partida.verPartidas.misPartidas

import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewItemMisPartidasBinding

import com.billarapp.partida.verPartidas.Partida

class MisPartidasHolder (private val binding: ViewItemMisPartidasBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(partida: Partida, misPartidasClickListener: (Partida)->Unit){

        binding.tvPartidaProvincia.text = partida.Provincia
        binding.tvPartidaLocalidad.text= partida.Localidad
        binding.tvPartidaLocal.text=partida.Local
        binding.tvPartidaFecha.text= partida.Fecha
        binding.tvPartidaHora.text=partida.Hora
        binding.tvPublicadoX2.text=partida.Jugador
        binding.tvPartidaNivel.text=partida.Nivel
        binding.tvOponente.text=partida.Candidatos



        itemView.setOnClickListener {
            misPartidasClickListener(partida)

            return@setOnClickListener
        }
    }
}
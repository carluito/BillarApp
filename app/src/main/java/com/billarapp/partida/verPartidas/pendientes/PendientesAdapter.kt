package com.billarapp.partida.verPartidas.pendientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewItemPartidaBinding
import com.billarapp.partida.verPartidas.Partida

class PendientesAdapter  (private val partidasLista: ArrayList<Partida>, private val pendientesClickListener: (Partida)->Unit):
    RecyclerView.Adapter<PendientesHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendientesHolder {

        val binding= ViewItemPartidaBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent, false)
        return PendientesHolder(binding)
    }

    override fun onBindViewHolder(holder: PendientesHolder, position: Int) {

        val partidaPendientes: Partida = partidasLista[position]
        holder.bind(partidaPendientes,pendientesClickListener)

    }

    override fun getItemCount(): Int {
        return partidasLista.size
    }

}
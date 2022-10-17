package com.billarapp.partida.verPartidas.adapterBuscarPartidas

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewItemPartidaBinding
import com.billarapp.partida.verPartidas.Partida

class BuscarPartidasAdapter (private val partidasLista: ArrayList<Partida>, private val buscarPartidaClickListener: (Partida)->Unit):RecyclerView.Adapter<BuscarPartidaHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuscarPartidaHolder {

        val binding= ViewItemPartidaBinding.inflate(LayoutInflater.from(parent.context)
            ,parent, false)
        return BuscarPartidaHolder(binding)
    }

    override fun onBindViewHolder(holder: BuscarPartidaHolder, position: Int) {

        val partidaBuscar: Partida = partidasLista[position]
        holder.bind(partidaBuscar,buscarPartidaClickListener)

    }

    override fun getItemCount(): Int {
        return partidasLista.size
    }


}
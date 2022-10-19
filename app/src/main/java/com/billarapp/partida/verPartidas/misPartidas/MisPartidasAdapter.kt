package com.billarapp.partida.verPartidas.misPartidas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewItemMisPartidasBinding
import com.billarapp.partida.verPartidas.Partida


class MisPartidasAdapter (private val partidasLista: ArrayList<Partida>, private val misPartidasClickListener: (Partida)->Unit):
    RecyclerView.Adapter<MisPartidasHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisPartidasHolder {

        val binding= ViewItemMisPartidasBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent, false)
        return MisPartidasHolder(binding)
    }

    override fun onBindViewHolder(holder: MisPartidasHolder, position: Int) {

        val partidaBuscar: Partida = partidasLista[position]
        holder.bind(partidaBuscar,misPartidasClickListener)

    }

    override fun getItemCount(): Int {
        return partidasLista.size
    }


}
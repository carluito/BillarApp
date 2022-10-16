package com.billarapp.partida.crearPartida.adapterCrearParitda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewMesasItemPartidaBinding
import com.billarapp.mesasBillar.Mesa

class PartidaAdapter(private val localLista: ArrayList<Mesa>
    , private val partidaClickListener: (Mesa)->Unit )    //crea objetos ViewHolder para las vistas y establece los datos para cada una
    : RecyclerView.Adapter<PartidaHolder>() {

    //Métodos del adapter para vincular los datos a las vistas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidaHolder {    //Crea un viewHolder nuevo y una vista asociada inicilizandola pero sin datos
        val binding = ViewMesasItemPartidaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PartidaHolder(binding)
    }

    override fun onBindViewHolder(holder: PartidaHolder, position: Int) {             //Vincula la vista a los datos
        val mesaBillar: Mesa =localLista[position]
        holder.bind(mesaBillar,partidaClickListener)

    }

    override fun getItemCount(): Int {      //Devuelve el tamaño de los datos
        return localLista.size      //devuelve nº de mesas
    }

}

package com.billarapp.mesasBillar.adapterMesas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewMesasItemBinding
import com.billarapp.mesasBillar.Mesa

class MesasAdapter(private val mesasBillarlista: ArrayList<Mesa>/*, private val mesaClickedListener: MesaClickedListener*/)     //crea objetos ViewHolder para las vistas y establece los datos para cada una
    : RecyclerView.Adapter<MesasHolder>() {
    //Métodos del adapter para vincular los datos a las vistas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesasHolder {    //Crea un viewHolder nuevo y una vista asociada inicilizandola pero sin dartos
        val binding = ViewMesasItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MesasHolder(binding)
    }

    override fun onBindViewHolder(holder: MesasHolder, position: Int) {             //Vincula la vista a los datos
        val mesaBillar: Mesa =mesasBillarlista[position]
        holder.bind(mesaBillar)
        //  holder.itemView.setOnClickListener { mesaClickedListener.onMesaClicked(mesaBillar) } //Para la interface mesaclickListener
    }

    override fun getItemCount(): Int {      //Devuelve el tamaño de los datos
        return mesasBillarlista.size      //devuelve nº de mesas
    }



}
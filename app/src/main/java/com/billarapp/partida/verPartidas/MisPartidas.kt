package com.billarapp.partida.verPartidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.R
import com.billarapp.databinding.FragmentMisPartidasBinding

class MisPartidas : Fragment() {

            private lateinit var partidasLista: ArrayList<Partida>
            private lateinit var _binding:FragmentMisPartidasBinding
            private val binding get()= _binding!!
            private lateinit var adaptadorRv: ViewPagerAdapter

    companion object {
        private const val ARG_OBJECT = "object"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_partidas, container, false)
    }
   /* private fun cargarMesas(){


        binding.rvMisPartidas.layoutManager = LinearLayoutManager(VerPartidas())          //Administrador de diseño q organiza los elementos de la lista

        binding.rvMisPartidas.setHasFixedSize(true)



        partidasLista = arrayListOf()

        adaptadorRv = ViewPagerAdapter(VerPartidas())// { mesa -> onItemSelected(mesa) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvMisPartidas.adapter = adaptadorRv



    }*/

}
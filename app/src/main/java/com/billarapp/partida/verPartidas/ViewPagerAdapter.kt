package com.billarapp.partida.verPartidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.billarapp.partida.verPartidas.buscarPartidas.BuscarPartida
import com.billarapp.partida.verPartidas.misPartidas.MisPartidas
import com.billarapp.partida.verPartidas.pendientes.Pendientes



class ViewPagerAdapter(fa: VerPartidas) : FragmentStateAdapter(fa) {




    override fun getItemCount(): Int = 3                                //3 solapas

    override fun createFragment(position: Int): Fragment {

        return when (position + 1) {                                    //Establecemos el layout en función de la solapa q sea

            1 -> MisPartidas()

            2 -> BuscarPartida()

            3-> Pendientes()


            else -> BuscarPartida()
        }

    }


}

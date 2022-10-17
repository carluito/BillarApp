package com.billarapp.partida.verPartidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.billarapp.partida.verPartidas.adapterBuscarPartidas.BuscarPartidasAdapter

 class ViewPagerAdapter(fa: VerPartidas) : FragmentStateAdapter(fa) {



    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position + 1) {                                    //Establecemos el layout en función de la solapa q sea

            1 -> BuscarPartida()

            2 -> Pendientes()

            else -> MisPartidas()


        }

    }



}
      /*  val fragment=ObjectFragment()
        fragment.arguments= Bundle().apply{

            putInt(ARG_OBJECT,position +1)
        }
        return fragment*/



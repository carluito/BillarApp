package com.billarapp.partida.verPartidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.billarapp.partida.verPartidas.buscarPartidas.BuscarPartida
import com.billarapp.partida.verPartidas.misPartidas.MisPartidas
import com.billarapp.partida.verPartidas.pendientes.Pendientes



class ViewPagerAdapter(fa: VerPartidas) : FragmentStateAdapter(fa) {

    /*private lateinit var db:FirebaseFirestore
    private lateinit var mAuth:FirebaseAuth
    companion object{

        private var nombreOponente:String?=null


    }*/

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        /*mAuth =
            FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
        val email = mAuth.currentUser?.email.toString()
         nombreOponente=cogerNombreOponente(email)

println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+ nombreOponente)
        val fragment=BuscarPartida()
        fragment.arguments= Bundle().apply{

            putInt(nombreOponente,1)
        }*/
        return when (position + 1) {                                    //Establecemos el layout en función de la solapa q sea

            1 -> BuscarPartida()

            2 -> Pendientes()

           3-> MisPartidas()


            else -> BuscarPartida()
        }

    }


}
/*  val fragment=ObjectFragment()
  fragment.arguments= Bundle().apply{
      putInt(ARG_OBJECT,position +1)
  }
  return fragment*/

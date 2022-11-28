package com.billarapp.partida.verPartidas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.billarapp.MainActivity
import com.billarapp.R
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityVerPartidasBinding
import com.google.android.material.tabs.TabLayoutMediator

class VerPartidas : AppCompatActivity() {

    private lateinit var binding:ActivityVerPartidasBinding

    private val adapter by lazy {ViewPagerAdapter(this) }                   //Adptador de la actividad viewpagerAdapter Para inicializarlo sólo en el momento que lo use


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerPartidasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter=adapter                                                               //adaptador para

        val tabLayoutMediator=TabLayoutMediator(binding.tabs,binding.pager) { tab, position ->
            when (position +1) {
                1 -> {
                    tab.text = "Mis Partidas"
                    tab.setIcon(R.drawable.mias)

                }
                2 -> {
                    tab.text = "Buscar"
                    tab.setIcon(R.drawable.ic_baseline_search_24)
                }
                3 -> {
                    tab.text = "Pendientes"
                    tab.setIcon(R.drawable.decision)
                }
            }
        }
        tabLayoutMediator.attach()

    }

    override fun onBackPressed() {                              //Sobreescribimos el método onBackPressed para q al pulsar el botón retroceso vuelva a MainActivity y no a la Main activity con el aviso
        super.onBackPressed()
        val intentVerPartidas= Intent(this, MainActivity::class.java)
        startActivity((intentVerPartidas))
    }

}
package com.billarapp.partida.verPartidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.R
import com.billarapp.databinding.ActivityVerPartidasBinding
import com.billarapp.partida.verPartidas.adapterBuscarPartidas.BuscarPartidasAdapter
import com.google.android.material.tabs.TabLayoutMediator

class VerPartidas : AppCompatActivity() {

    private lateinit var binding:ActivityVerPartidasBinding

    private val adapter by lazy {ViewPagerAdapter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerPartidasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter=adapter

        val tabLayoutMediator=TabLayoutMediator(binding.tabs,binding.pager) { tab, position ->
            when (position +1) {
                1 -> {
                    tab.text = "Buscar"
                    tab.setIcon(R.drawable.ic_baseline_search_24)
                    /*val badge:BadgeDrawable= tab.orCreateBadge                                        //Badge a modo de aviso
                    badge.backgroundColor=ContextCompat.getColor(applicationContext,R.color.CadetBlue)
                    badge.isVisible=true*/
                }
                2 -> {
                    tab.text = "Pendientes"
                    tab.setIcon(R.drawable.decision)
                }
                3 -> {
                    tab.text = "Mis Partidas"
                    tab.setIcon(R.drawable.mias)
                }
            }
        }
        tabLayoutMediator.attach()






        }


    }

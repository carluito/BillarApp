package com.billarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.billarapp.databinding.ActivityPartidasBinding

class Partidas : AppCompatActivity() {

    private lateinit var binding: ActivityPartidasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPartidasBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}
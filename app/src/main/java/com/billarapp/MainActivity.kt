package com.billarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.billarapp.databinding.ActivityMainBinding
import com.billarapp.mesasBillar.BuscarMesa
import com.billarapp.registroDatos.UneteRegistro

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding //Modificador Private para que solo sea visible en la clase actual y lateinit
    //para iniciarla por fuera del constructor con seguridad en caso de que sea no anulable
    private lateinit var intentMain : Intent
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)



        bindingMain.btnUnirse.setOnClickListener{

            intentMain = Intent(this, UneteRegistro::class.java)
            startActivity(intentMain)
            Toast.makeText(this, "Unete para echar una partida", Toast.LENGTH_SHORT).show()

        }

        bindingMain.btnMesas.setOnClickListener{

            intentMain = Intent (this, BuscarMesa::class.java)
            startActivity(intentMain)
            Toast.makeText(this,"Descubre dónde puedes jugar", Toast.LENGTH_SHORT).show()

        }
        bindingMain.btnInfo.setOnClickListener{ Toast.makeText(this,"ostia", Toast.LENGTH_SHORT).show()}

    }
}
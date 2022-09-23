package com.billarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.billarapp.databinding.ActivityCrearPartidaBinding

class CrearPartida : AppCompatActivity() {

    private lateinit var bindingPartidas: ActivityCrearPartidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPartidas =ActivityCrearPartidaBinding.inflate(layoutInflater)
        setContentView(bindingPartidas.root)

        spProvincia()

        bindingPartidas.etHora.setOnClickListener(){
            ponerHora()
        }

        bindingPartidas.etFecha.setOnClickListener(){
            ponerFecha()
        }


    }

    private fun ponerFecha() {
        val ponFecha = PonerFechaFragment { dia, mes, anho -> fechaSeleccionada(dia, mes, anho) }
        ponFecha.show(supportFragmentManager,"fecha")
    }
    private fun fechaSeleccionada(dia: Int, mes: Int, anho: Int) {

        bindingPartidas.etFecha.setText("$dia-$mes-$anho")

    }

    private fun ponerHora(){
        val ponHora = PonerHoraFragment{horas,minutos-> horaSeleccionada(horas,minutos)}
        ponHora.show(supportFragmentManager,"hora")
    }

    private fun horaSeleccionada(horas:Int,minutos:Int) {

        bindingPartidas.etHora.setText(if(minutos>=10){
            "$horas:$minutos"
        }else{                                                  //Para poner un 0 en caso de que los minutos sean menos de 10
            "$horas:0$minutos"
        })


    }

    private fun spProvincia(){

        val provinciaSp = bindingPartidas.spProvinciaPartidas
        val listaprovincias= resources.getStringArray(R.array.provinciaDJ)
        val adaptadorProvincias: ArrayAdapter<*> = ArrayAdapter(this,android.R.layout.simple_spinner_item, listaprovincias)
        provinciaSp.adapter= adaptadorProvincias

        provinciaSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, posicion: Int, p3: Long) {
                if(posicion!=0){
                    Toast.makeText(this@CrearPartida, "Provincia seleccionada : "+listaprovincias[posicion],
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO saber q hacer para q guarde tos los datos del jugador
            }
        }

    }
}
package com.billarapp.registroDatos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.billarapp.MainActivity
import com.billarapp.R
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityDatosJugadorBinding
import com.google.firebase.firestore.FirebaseFirestore

enum class tipoProveedor {
    EMAIL,
    GOOGLE
}

class DatosJugador : AppCompatActivity() {



    private lateinit var bindingUnete: ActivityDatosJugadorBinding
    private val db= FirebaseFirestore.getInstance()
    private lateinit var intentDatos:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUnete = ActivityDatosJugadorBinding.inflate(layoutInflater)
        setContentView(bindingUnete.root)

        val paquete: Bundle?= intent.extras
        val email: String?=paquete?.getString("email",null)
        val proveedor: String?= paquete?.getString("proveedor",null)


        ponerEmailProveedor(email?:"",proveedor?:"")

        spProvincia()

        spNivel()

        spDisponibilidad()

        guardar(email?:"", proveedor ?:"")


    }





    private fun spNivel(){

        val nivelSp = bindingUnete.spNivel
        val listaNivel = resources.getStringArray(R.array.nivel)
        val adaptadorNivel: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listaNivel)
        nivelSp.adapter = adaptadorNivel

        nivelSp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>,
                vista: View,
                posicion: Int,
                id: Long
            ) {

                if (posicion != 0) {
                    Toast.makeText(
                        this@DatosJugador,
                        getString(R.string.seleccion) + " " + " " +   //Para pillar los datos del array guardado en string.xml y seleccionar lo escogido
                                listaNivel[posicion],
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@DatosJugador, "No quiere", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun spProvincia(){

        val provinciaSp = bindingUnete.spProvincia
        val listaprovincias= resources.getStringArray(R.array.provinciaDJ)
        val adaptadorProvincias: ArrayAdapter<*> = ArrayAdapter(this,android.R.layout.simple_spinner_item, listaprovincias)
        provinciaSp.adapter= adaptadorProvincias

        provinciaSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, posicion: Int, p3: Long) {
                if(posicion!=0){
                    Toast.makeText(this@DatosJugador, "Provincia seleccionada : "+listaprovincias[posicion],Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO saber q hacer para q guarde tos los datos del jugador
            }
        }

    }







    private fun spDisponibilidad(){

        val spDisponibilidad = bindingUnete.spDisponibilidad
        val listaDisponibilidad = resources.getStringArray(R.array.disponibilidad)
        val adaptadorDisponibilidad: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaDisponibilidad)
        spDisponibilidad.adapter = adaptadorDisponibilidad

        spDisponibilidad.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO saber q hacer para q guarde tos los datos del jugador
            }


        }

    }






    private fun guardar(email: String?,proveedor:String?) {


        bindingUnete.btnGuardar.setOnClickListener {

            if (bindingUnete.etJugador.text.toString()==""|| bindingUnete.spNivel.selectedItem.toString()=="" || bindingUnete.spDisponibilidad.
                selectedItem.toString() == ""|| bindingUnete.spProvincia.selectedItem.toString() == "" || bindingUnete.etLocalidad.text.toString()=="") {                                                                                                                                                //Para meter todos los usuarios en la colección correspondiente de Firestore


                Toast.makeText(this,"Así no se puede, rellena tos los campos tronc@",Toast.LENGTH_SHORT).show()

            }else{
                if (email != null) {
                    db.collection("Usuarios").document(email).set(
                        hashMapOf(
                            /*"Proveedor" to proveedor,*/
                            "Nombre" to bindingUnete.etJugador.text.toString(),
                            "Disponibilidad" to bindingUnete.spDisponibilidad.selectedItem.toString(),
                            "Provincia" to bindingUnete.spProvincia.selectedItem.toString(),
                            "Localidad" to bindingUnete.etLocalidad.text.toString(),
                            "Nivel" to bindingUnete.spNivel.selectedItem.toString()
                        )

                    )

                    aUsuario(email,proveedor)                       // Para pasar a la pantalla de Usuario

                }
            }
        }
    }







    private fun ponerEmailProveedor(email: String?, proveedor: String?){                          //Para q ponga el email y proveedor en los textview de Datosjugador

        bindingUnete.tvEmailDatos.text=email
        bindingUnete.tvProveedorDatos.text=proveedor








    }
    private fun aUsuario(email: String?, proveedor: String?){


        intentDatos= Intent(this, Usuario::class.java).apply {
            putExtra("email", email)
            putExtra("proveedor",proveedor )
        }
        startActivity(intentDatos)

    }
}
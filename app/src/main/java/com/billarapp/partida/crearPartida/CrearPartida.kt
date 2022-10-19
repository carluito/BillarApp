package com.billarapp.partida.crearPartida

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityCrearPartidaBinding
import com.billarapp.mesasBillar.Mesa
import com.billarapp.partida.crearPartida.adapterCrearParitda.PartidaAdapter
import com.billarapp.partida.verPartidas.Partida
import com.billarapp.partida.verPartidas.ViewPagerAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class CrearPartida : AppCompatActivity() {

    private lateinit var bindingPartidas: ActivityCrearPartidaBinding
    private lateinit var db: FirebaseFirestore
    private var provinciaSeleccion: String? = null
    private var localidadSeleccion: String? = null
    private lateinit var intentPartida: Intent
    private lateinit var localLista: ArrayList<Mesa>
    private lateinit var adaptadorPartida: PartidaAdapter
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPartidas = ActivityCrearPartidaBinding.inflate(layoutInflater)
        setContentView(bindingPartidas.root)

        cargarMesas()

        bindingPartidas.etHora.setOnClickListener() {
            ponerHora()
        }

        bindingPartidas.etFecha.setOnClickListener() {
            ponerFecha()
        }

          spinnerProvincias()

        localLista.clear()


        bindingPartidas.btnBucarPartida.setOnClickListener(){


           // cogerNivelyNombre()

            if(bindingPartidas.etFecha.text.toString().isNotEmpty()&&bindingPartidas.etHora.text.toString().isNotEmpty()) {
                localLista.clear()
                mesaXLocalidad()

            }else{
                Toast.makeText(this,"Pon fecha y hora",Toast.LENGTH_SHORT).show()
            }
        }
    }





    private fun cargarMesas(){


        bindingPartidas.rvPartida.layoutManager = LinearLayoutManager(this)          //Administrador de diseño q organiza los elementos de la lista

        bindingPartidas.rvPartida.setHasFixedSize(true)



        localLista = arrayListOf()

        adaptadorPartida = PartidaAdapter(localLista) { mesa -> partidaSeleccionada(mesa) }                                 // Creamos el objeto de Nuestra clase MesasAdapter


            bindingPartidas.rvPartida.adapter = adaptadorPartida




    }

/*private fun cogerNivelyNombre() {
    mAuth =
        FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
    val email = mAuth.currentUser?.email.toString()


    var nombre: String? = null
    var nivel: String?=null


    if (email != null) {
        db=FirebaseFirestore.getInstance()
        db.collection("Usuarios").document(email).get().addOnSuccessListener {

            nivel =
                (it.get("Nivel") as String?)                                                                //Para coger nombre y nivel de la colección Usuarios
            nombre = (it.get("Nombre") as String?)

            println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" + nombre)


        }
    }
}*/


private fun partidaSeleccionada(mesa: Mesa){

    val paqueteCrear= intent.extras
    val email: String? = paqueteCrear?.getString("email")
    val proveedor:String? = paqueteCrear?.getString("proveedor")

    var nombre:String? =null
    var nivel: String? =null


    if (email != null) {
        db.collection("Usuarios").document(email).get().addOnSuccessListener {

            nivel = (it.get("Nivel") as String?)                                                                //Para coger nombre y nivel de la colección Usuarios
            nombre=(it.get("Nombre") as String?)



            db.collection("Partidas").document(mesa.Local+bindingPartidas.etFecha.text.toString()+bindingPartidas.etHora.text.toString()).set(                                               //dejamos document vacío para q genere un id automaticamente
                hashMapOf(
                    "Fecha" to bindingPartidas.etFecha.text.toString(),
                    "Hora" to bindingPartidas.etHora.text.toString(),
                    "Local" to (mesa.Local).toString(),
                    "Jugador" to nombre,
                    "Nivel" to nivel,
                    "Localidad" to (mesa.Localidad).toString(),
                    "Provincia" to (mesa.Provincia),
                    "Email" to email,
                    "Candidatos" to "Candidatos"
                 )
            )
        }
    }

    Toast.makeText(this,"Partida Publicada",Toast.LENGTH_SHORT).show()
    intentPartida= Intent(this,Usuario::class.java).apply{
        putExtra("email",email)
        putExtra("proveedor",proveedor)
    }
    startActivity(intentPartida)
}



    private fun ponerFecha() {
        val ponFecha = PonerFechaFragment { dia, mes, anho -> fechaSeleccionada(dia, mes, anho) }
        ponFecha.show(supportFragmentManager, "fecha")

    }






    private fun fechaSeleccionada(dia: Int, mes: Int, anho: Int) {

        val calendario= Calendar.getInstance()
        val anhoHoy=calendario.get(Calendar.YEAR)
        val diaHoy=calendario.get(Calendar.DAY_OF_MONTH)
        val mesHoy=calendario.get(Calendar.MONTH)


        if(anho<anhoHoy||mes<mesHoy+1||dia<diaHoy){
            Toast.makeText(this,"Fecha no válida, éso es el pasado", Toast.LENGTH_SHORT).show()                                     //Para q la fecha escogida sea siempre posterior a la actual

        }else {

            bindingPartidas.etFecha.setText("$dia-$mes-$anho")
        }


    }







        private fun ponerHora() {

            val ponHora = PonerHoraFragment { horas, minutos -> horaSeleccionada(horas, minutos) }
            ponHora.show(supportFragmentManager, "hora")

        }







        private fun horaSeleccionada(horas: Int, minutos: Int) {
                    val hoy = LocalDateTime.now()                                   //Para saber el dia actual
                    val ahora= Instant.now()                                        // Para saber la hora en ese momento




                    if (horas>=2&&horas<9){
                        Toast.makeText(this,"No creo que esté abierto", Toast.LENGTH_SHORT).show()
                    }else {
                        bindingPartidas.etHora.setText(
                            if (minutos >9) {
                                 "$horas:$minutos"
                            } else {                                                  //Para poner un 0 en caso de que los minutos sean menos de 10
                                 "$horas:0$minutos"
                            })
                        }
             }






    private fun mesaXLocalidad(){
        db=FirebaseFirestore.getInstance()
        db.collection("Mesas").whereEqualTo("Localidad",localidadSeleccion).addSnapshotListener(object:
            EventListener<QuerySnapshot> {

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Error de Firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        localLista.add(dc.document.toObject(Mesa::class.java))

                    }
                }

                adaptadorPartida.notifyDataSetChanged()
            }
        })
    }





    private fun spinnerProvincias(){

            val rootProvinciaRef = FirebaseFirestore.getInstance()
            val provinciaRef = rootProvinciaRef.collection("Mesas")
            val spProvincia = bindingPartidas.spProvinciaPartidas
            val listaProvincias: MutableList<String?> = ArrayList()
            val adapter =
                ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    listaProvincias
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spProvincia.adapter = adapter
            provinciaRef.get().addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (document in task.result) {


                        val provincia = document.getString("Provincia")

                        if (listaProvincias.contains(provincia)) {
                            println("ListaProvincias " + listaProvincias)
                        } else {

                            listaProvincias.add(provincia)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
            spProvincia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                    provinciaSeleccion = listaProvincias[position]

                    spinnerLocalidad()


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Nada seleccionado")
                }

            }


    }






    private fun spinnerLocalidad(){



        val rootLocalidadRef = FirebaseFirestore.getInstance()
        val localidadRef = rootLocalidadRef.collection("Mesas").whereEqualTo("Provincia",provinciaSeleccion)
        val spLocalidades= bindingPartidas.spLocalidadCrear
        val listaLocalidades: MutableList<String?> = ArrayList()
        val adapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listaLocalidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLocalidades.adapter = adapter
        localidadRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
            if (task.isSuccessful) {


                for (document in task.result) {
                    val localidad = document.getString("Localidad")

                    if(listaLocalidades.contains(localidad)){
                        println("localidad"+localidad)
                    }else {

                        listaLocalidades.add(localidad)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })



        spLocalidades.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                    localidadSeleccion = listaLocalidades[position]


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nada seleccionado")
            }


        }

    }

}
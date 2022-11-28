package com.billarapp.partida.crearPartida



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityCrearPartidaBinding
import com.billarapp.mesasBillar.Mesa
import com.billarapp.partida.crearPartida.adapterCrearParitda.PartidaAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class CrearPartida : AppCompatActivity() {

    private lateinit var bindingPartidas: ActivityCrearPartidaBinding
    private lateinit var db: FirebaseFirestore
    private var provinciaSeleccion: String? = null                                  //Variable para guardar la provincia seleccionada
    private var localidadSeleccion: String? = null                                  //Variable para guardar la localidad seleccionada
    private lateinit var intentPartida: Intent
    private lateinit var localLista: ArrayList<Mesa>
    private lateinit var adaptadorPartida: PartidaAdapter
    private var esHoy:Boolean=false                                                 //Variable para saber si el dia es hoy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPartidas = ActivityCrearPartidaBinding.inflate(layoutInflater)
        setContentView(bindingPartidas.root)

        cargarMesas()

        ponerFechayHora()

        spinnerProvincias()

        localLista.clear()

        crearPartida()


    }
private fun ponerFechayHora(){
    bindingPartidas.etFecha.setOnClickListener() {
        ponerFecha()
        bindingPartidas.etHora.setOnClickListener() {
            ponerHora()
        }
    }
}
private fun crearPartida(){

    bindingPartidas.btnCrearPartida.setOnClickListener(){

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

        adaptadorPartida = PartidaAdapter(localLista) { mesa -> mesaSeleccionada(mesa) }                                 // Creamos el objeto de Nuestra clase MesasAdapter


            bindingPartidas.rvPartida.adapter = adaptadorPartida




    }



private fun mesaSeleccionada(mesa: Mesa){                                        // En cuanto seleccionemos una mesa del recycler view nos creará la partida añadiendo todos los datos necesarios

    val fecha =bindingPartidas.etFecha.text.toString()                              //Convertimos tanto la fecha como la hora de String a Date
    val hora=bindingPartidas.etHora.text
    val sdf=SimpleDateFormat("dd-MM-yyyy hh:mm")
    val fechaHora= "$fecha $hora:00"
    val timestamp=sdf.parse(fechaHora)

    val paqueteCrear= intent.extras
    val email: String? = paqueteCrear?.getString("email")
    val proveedor:String? = paqueteCrear?.getString("proveedor")

    var nombre:String?
    var nivel: String?

    val builder = AlertDialog.Builder(this as Context)                                  //Alert dialog con dos botones para confirmar la mesa
    builder.setTitle("¿Seleccionar?")
    builder.setMessage("¿Quieres seleccionar esta mesa?")

    builder.setPositiveButton("Cancelar") { _, _ ->

    }
    builder.setNegativeButton("Aceptar") { _, _ ->

        if (email != null) {
            db.collection("Usuarios").document(email).get().addOnSuccessListener {

                nivel =
                    (it.get("Nivel") as String?)                                                                //Para coger nombre y nivel de la colección Usuarios
                nombre = (it.get("Nombre") as String?)



                db.collection("Partidas")
                    .document(mesa.Local + bindingPartidas.etFecha.text.toString() + bindingPartidas.etHora.text.toString())
                    .set(
                        hashMapOf(
                            "Fecha" to bindingPartidas.etFecha.text.toString(),
                            "Hora" to bindingPartidas.etHora.text.toString(),
                            "FechaHora" to timestamp,
                            "Local" to (mesa.Local).toString(),
                            "Jugador" to nombre,
                            "Nivel" to nivel,
                            "Localidad" to (mesa.Localidad).toString(),
                            "Provincia" to (mesa.Provincia),
                            "Email" to email,
                            "Candidatos" to false,
                            "Jugada" to false
                        )
                    )
            }
        }
        Toast.makeText(this,"Partida Publicada",Toast.LENGTH_SHORT).show()



        intentPartida= Intent(this,Usuario::class.java).apply{                                                          //Para ir a la activity Usuario al ser publicada
            putExtra("email",email)
            putExtra("proveedor",proveedor)
        }
        startActivity(intentPartida)
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()


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

        if(anho==anhoHoy&&mes==mesHoy+1&&dia==diaHoy){                                      //Para saber si es hoy

            esHoy=true
            bindingPartidas.etFecha.setText("$dia-$mes-$anho")
        }else{
            if(anho==anhoHoy){
                if(mes>(mesHoy+1)) {
                    esHoy = false
                    bindingPartidas.etFecha.setText("$dia-$mes-$anho")

                }else if(mes==(mesHoy+1)){
                    if(dia>diaHoy){
                        esHoy = false
                        bindingPartidas.etFecha.setText("$dia-$mes-$anho")
                    }else{
                        esHoy=false
                        Toast.makeText(this,"Fecha no válida, éso es el pasado", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    esHoy=false
                    Toast.makeText(this,"Fecha no válida, éso es el pasado", Toast.LENGTH_SHORT).show()
                }
            }else if(anho>anhoHoy){
                esHoy = false
                bindingPartidas.etFecha.setText("$dia-$mes-$anho")
            }
            else{
                esHoy=false
                Toast.makeText(this,"Fecha no válida, éso es el pasado", Toast.LENGTH_SHORT).show()
            }
        }


    }







        private fun ponerHora() {

            val ponHora = PonerHoraFragment { horas, minutos -> horaSeleccionada(horas, minutos) }
            ponHora.show(supportFragmentManager, "hora")

        }







        private fun horaSeleccionada(horas: Int, minutos: Int) {
                    val calendario = Calendar.getInstance()
                    val horaActual=calendario.get(Calendar.HOUR_OF_DAY)
                    val ahora= LocalTime.now()                                                           // Para saber la hora en ese momento
                    val horaString =
                        if(horas<10){                                                                    // Hora a String para compararla con LocalTime
                         if(minutos<10){                                                                 // Añadimos 0 en horas o en minutos depende para q pueda parsearla sin problema
                             "0"+horas.toString()+":0"+minutos.toString()+":"+"00"

                         }else{
                            "0"+horas.toString()+":"+minutos.toString()+":"+"00"
                            }
                        }else {
                                if(minutos<10){
                                    horas.toString()+":0"+minutos.toString()+":"+"00"

                                }else{
                                    horas.toString()+":"+minutos.toString()+":"+"00"
                                }
                        }
                    val horaHoy= LocalTime.parse(horaString)


            if(esHoy) {                                                                                     //Para saber el dia escogido es hoy
                    if (horaHoy.isBefore(ahora)) {                                                                                                                                        //Para comparar la hora escogida x si es el pasado


                        Toast.makeText(this, "Éso es el pasado", Toast.LENGTH_SHORT).show()

                    }

                    else{
                         if(horas>2&&horas<9){
                            Toast.makeText(this,"No creo que esté abierto", Toast.LENGTH_SHORT).show()
                        }
                        else if((horas-horaActual)<2){


                            Toast.makeText(this, "Muy ajustada,pero tú mandas", Toast.LENGTH_LONG).show()                                  //Aviso para q deje 2 horas de margen

                            bindingPartidas.etHora.setText(
                                if (minutos <10) {
                                    "$horas:0$minutos"
                                } else {                                                                                                                                                         //Para poner un 0 en caso de que los minutos sean menos de 10
                                    "$horas:$minutos"
                                })
                        }else{

                        bindingPartidas.etHora.setText(
                            if (minutos <10) {
                                "$horas:0$minutos"
                            } else {                                                                                                                                                         //Para poner un 0 en caso de que los minutos sean menos de 10
                                "$horas:$minutos"
                            })
                        }
                        }
                }else{
                        if (horas>2&&horas<9){
                            Toast.makeText(this,"No creo que esté abierto", Toast.LENGTH_SHORT).show()
                        }else {
                            bindingPartidas.etHora.setText(
                                if (minutos <10) {
                                    "$horas:0$minutos"
                                } else {                                                  //Para poner un 0 en caso de que los minutos sean menos de 10
                                    "$horas:$minutos"
                                })
                        }
                    }



        }




    private fun mesaXLocalidad(){                                                                                       //Llenar el el recyclerView con mesas x localidad
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





    private fun spinnerProvincias(){                                                                                //Cargar spinner Con las provincias donde haya mesa

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

                    listaProvincias.add("")

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

                listaLocalidades.add("")

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
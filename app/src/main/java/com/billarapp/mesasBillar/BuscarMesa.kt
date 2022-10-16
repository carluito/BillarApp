package com.billarapp.mesasBillar



import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.databinding.ActivityBuscarMesaBinding
import com.billarapp.mesasBillar.adapterMesas.MesasAdapter
import com.google.firebase.firestore.*
import com.google.android.gms.tasks.OnCompleteListener

class BuscarMesa : Activity() { //La clase Activity se encarga de crear una ventana en la que puede colocar su interfaz de usuario

    private lateinit var bindingBuscarMesa: ActivityBuscarMesaBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mesasBillarlista: ArrayList<Mesa>
    private lateinit var adaptadorRv: MesasAdapter
    private var provinciaSeleccion: String? = null        //Variable para sacar el cambio de Provincia en el spinner de provincia
    private var localidadSeleccion: String? = null        //  Variable para sacar el cambio de Localidad en el spinner de provincia
    private lateinit var intentBuscar: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingBuscarMesa = ActivityBuscarMesaBinding.inflate(layoutInflater)
        setContentView(bindingBuscarMesa.root)                                                      //con esto es con lo que la clase Activity coloca la UI


        cargarMesas()

        spinnerProvincias()

        bindingBuscarMesa.btnBuscarMesa.setOnClickListener {

            Toast.makeText(this,"Mantén pulsado en cada local para más información",Toast.LENGTH_SHORT).show()

            if(provinciaSeleccion=="Todas"){                                                // Para cuando selecciones todas salgan todas las mesas y si no x provincia

                todasMesas()

            }else{
                                                                       //Para que rellene el spinner con las localidades de ésa provincia
                if(localidadSeleccion!=""){
                    mesaXLocalidad()
                }else{
                    mesaXProvincia()
                }
            }
            mesasBillarlista.clear()
        }



    }














    private fun onItemSelected(mesa:Mesa){                                                          //Para q al hacer long click vaya al navegador a buscarlo x nombre del local y localidad


        intentBuscar = Intent(Intent.ACTION_WEB_SEARCH)
        val busca= (mesa.Local+" "+mesa.Localidad)
        intentBuscar.putExtra(SearchManager.QUERY, busca)
        startActivity(intentBuscar)
    }







    private fun cargarMesas(){


        bindingBuscarMesa.rvBuscarMesa.layoutManager = LinearLayoutManager(this)          //Administrador de diseño q organiza los elementos de la lista

        bindingBuscarMesa.rvBuscarMesa.setHasFixedSize(true)



        mesasBillarlista = arrayListOf()

        adaptadorRv =MesasAdapter(mesasBillarlista) { mesa -> onItemSelected(mesa) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        bindingBuscarMesa.rvBuscarMesa.adapter = adaptadorRv



    }








    private fun mesaXLocalidad(){
        db=FirebaseFirestore.getInstance()
        db.collection("Mesas").whereEqualTo("Localidad",localidadSeleccion).addSnapshotListener(object:EventListener<QuerySnapshot>{

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Error de Firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        mesasBillarlista.add(dc.document.toObject(Mesa::class.java))

                    }
                }

                adaptadorRv.notifyDataSetChanged()
            }
        })
    }






    private fun mesaXProvincia() {                                            //Método para ver las mesas pasandole el valorProvincia para q escoja x provincias

        db = FirebaseFirestore.getInstance()
        db.collection("Mesas").whereEqualTo("Provincia",provinciaSeleccion).addSnapshotListener(object :
            EventListener<QuerySnapshot> {


            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Error de Firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        mesasBillarlista.add(dc.document.toObject(Mesa::class.java))

                    }
                }

                adaptadorRv.notifyDataSetChanged()
            }
        })

    }






    private fun todasMesas() {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor

        db = FirebaseFirestore.getInstance()
        db.collection("Mesas").
        addSnapshotListener(object : EventListener<QuerySnapshot> {



            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Error de Firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        mesasBillarlista.add(dc.document.toObject(Mesa::class.java))

                    }
                }

                adaptadorRv.notifyDataSetChanged()

            }
        })

    }







    private fun spinnerProvincias(){

        val rootProvinciaRef = FirebaseFirestore.getInstance()
        val provinciaRef = rootProvinciaRef.collection("Mesas")
        val spProvincia= bindingBuscarMesa.spProvinciaBuscar
        val listaProvincias: MutableList<String?> = ArrayList()
        val adapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listaProvincias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvincia.adapter = adapter
        provinciaRef.get().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                listaProvincias.add("Todas")                                         //Para que la primera opción del spinner sea buscar todas

                for (document in task.result) {


                    val provincia = document.getString("Provincia")

                    if(listaProvincias.contains(provincia)){
                       println("ListaProvincias "+listaProvincias)
                    }else {

                        listaProvincias.add(provincia)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
        spProvincia.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
              provinciaSeleccion= listaProvincias[position]

                println("Selección "+provinciaSeleccion)
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
 val spLocalidades= bindingBuscarMesa.spLocalidadBuscar
 val listaLocalidades: MutableList<String?> = ArrayList()
 val adapter =
     ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listaLocalidades)
 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
 spLocalidades.adapter = adapter
 localidadRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
     if (task.isSuccessful) {

            listaLocalidades.add("")                                                //Para que la primera opción del spinner esté en blanco

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
                    localidadSeleccion=listaLocalidades[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Nada seleccionado")
                }


            }
}


}

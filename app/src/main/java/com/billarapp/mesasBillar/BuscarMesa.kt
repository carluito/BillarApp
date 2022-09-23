package com.billarapp.mesasBillar


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.MainActivity
import com.billarapp.databinding.ActivityBuscarMesaBinding
import com.billarapp.mesasBillar.adapterMesas.MesasAdapter
import com.google.firebase.firestore.*
import com.billarapp.R

class BuscarMesa : Activity() { //La clase Activity se encarga de crear una ventana en la que puede colocar su interfaz de usuario

    private lateinit var bindingBuscarMesa: ActivityBuscarMesaBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mesasBillarlista: ArrayList<Mesa>
    private lateinit var adaptadorRv: MesasAdapter
    private var cambioProvincia: Int? = null        //Variable para sacar el cambio de Provincia en el spinner de provincia
    private lateinit var adaptadorProvincias: ArrayAdapter<*>
    private lateinit var intentBuscar: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingBuscarMesa = ActivityBuscarMesaBinding.inflate(layoutInflater)
        setContentView(bindingBuscarMesa.root)                                                      //con esto es con lo que la clase Activity coloca la UI



        bindingBuscarMesa.rvBuscarMesa.layoutManager = LinearLayoutManager(this)          //Administrador de diseño q organiza los elementos de la lista

        bindingBuscarMesa.rvBuscarMesa.setHasFixedSize(true)



        mesasBillarlista = arrayListOf()

        adaptadorRv =MesasAdapter(mesasBillarlista)                                             // Creamos el objeto de Nuestra clase MesasAdapter

        bindingBuscarMesa.rvBuscarMesa.adapter = adaptadorRv




        spinnerProvincias()



        bindingBuscarMesa.btnBuscarMesa.setOnClickListener {

            val localidade = bindingBuscarMesa.etLocalidadBuscarMesa.text.toString()                //Dentro dl click listener para q coja lo q se escribe

            if (bindingBuscarMesa.etLocalidadBuscarMesa.text.toString() == "") {

                when (cambioProvincia) {                                                                //Para pasar el cambio de provincia
                    0 -> todasMesas()
                    1 -> mesaXProvincia("A Coruña")
                    2 -> mesaXProvincia("Lugo")
                    3 -> mesaXProvincia("Ourense")
                    4 -> mesaXProvincia("Pontevedra")
                }
                mesasBillarlista.clear()

            }
            else{
                mesasBillarlista.clear()                    //para limpiar el edittext tb
                localidad(localidade)
                bindingBuscarMesa.etLocalidadBuscarMesa.setText("")//Para limpiar lo q escribe el usuario y así pueda volver a buscar con el spinner
            }

        }
        bindingBuscarMesa.btnVolverBuscar.setOnClickListener {
            intentBuscar= Intent(this, MainActivity::class.java)
            startActivity(intentBuscar)
        }

    }

    private fun mesaXProvincia(valorProvincia: String) {                                            //Método para ver las mesas pasandole el valorProvincia para q escoja x provincias

        db = FirebaseFirestore.getInstance()
        db.collection("Mesas").whereEqualTo("Provincia",valorProvincia).addSnapshotListener(object :
            EventListener<QuerySnapshot> {

            @SuppressLint("NotifyDataSetChanged")
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
    private fun localidad(localidad :String?){

        db= FirebaseFirestore.getInstance()

        db.collection("Mesas").whereEqualTo("Localidad", localidad).addSnapshotListener(object :
            EventListener<QuerySnapshot> {

            @SuppressLint("NotifyDataSetChanged")
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




    private fun todasMesas() {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor

        db = FirebaseFirestore.getInstance()
        db.collection("Mesas").
        addSnapshotListener(object : EventListener<QuerySnapshot> {

            @SuppressLint("NotifyDataSetChanged")
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

        val provinciaSp =bindingBuscarMesa.spProvinciaBuscar                                                                     //variables para el spinner de provincias
        val listaProvincias = resources.getStringArray(R.array.Provincia)
        adaptadorProvincias = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProvincias)

        provinciaSp.adapter = adaptadorProvincias

        provinciaSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                p0: AdapterView<*>,
                vista: View,
                posicionProvincias: Int,
                id: Long
            ) {
                when (posicionProvincias) {
                    0 -> cambioProvincia = 0
                    1 -> cambioProvincia = 1
                    2 -> cambioProvincia = 2
                    3 -> cambioProvincia = 3
                    4 -> cambioProvincia = 4

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@BuscarMesa, "Algo falló", Toast.LENGTH_SHORT).show()
            }

        }
    }

}

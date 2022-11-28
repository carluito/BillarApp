package com.billarapp.partida.verPartidas.buscarPartidas


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.databinding.FragmentBuscarPartidaBinding
import com.billarapp.partida.verPartidas.Partida
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.HashMap


class BuscarPartida : Fragment() {

        private lateinit var partidasLista: ArrayList<Partida>
        private var _binding: FragmentBuscarPartidaBinding? = null
        private val binding get() = _binding!!
        private lateinit var adaptadorRv: BuscarPartidasAdapter
        private var provinciaSeleccion: String? = null
        private var localidadSeleccion: String? = null
        private lateinit var mAuth: FirebaseAuth
        private lateinit var db:FirebaseFirestore


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentBuscarPartidaBinding.inflate(inflater, container, false)

            mAuth = FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
            val email=mAuth.currentUser?.email.toString()



            cargarMesas()                                                                       // Cargar los items

            spinnerProvincias(email)

            buscarPartida(email)


            return binding.root

        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        private fun buscarPartida(email: String?){
            binding.btnBuscarPartida.setOnClickListener() {

                mesaXLocalidad(email)

                partidasLista.clear()

            }

        }
        private fun cargarMesas() {


            binding.rvBuscarPartida.layoutManager =                                                 //Administrador de diseño q organiza los elementos de la lista

                LinearLayoutManager(activity as Context) //context

            binding.rvBuscarPartida.setHasFixedSize(true)

            partidasLista = arrayListOf()

            adaptadorRv =

                BuscarPartidasAdapter(partidasLista) { partida -> seleccionBuscarPartida(partida ) }                 // Creamos el objeto de Nuestra clase MesasAdapter

            binding.rvBuscarPartida.adapter = adaptadorRv


        }


        private fun seleccionBuscarPartida(partida: Partida) {                              //Para controlar la selección de los diferentes items
          alertDialogBuscar(partida)
        }

        private fun alertDialogBuscar(partida: Partida){                                    //Alert dialog para escoger o no esa partida
            mAuth =
                FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
            val email = mAuth.currentUser?.email.toString()


            var nombreee: String? = null                                                      //Variable para guardar el nombre de oonente



            db=FirebaseFirestore.getInstance()
            db.collection("Usuarios").document(email).get().addOnSuccessListener {


                nombreee =
                    (it.get("Nombre") as String?)                                               //Para coger el nombre del oponente
            }
                                                                                                //Alert dialog con dos botones para confirmar
            val builder = AlertDialog.Builder(activity as Context)                              //Creamos la instancia del builder con su constructor y un contexto
            builder.setTitle("¿Jugar?")
            builder.setMessage("¿Quieres jugar esta partida?")

            builder.setPositiveButton("Cancelar") { _, _ ->                                 //Metodos para establecer los botones

            }
            builder.setNegativeButton("Aceptar") { _, _ ->

                db=FirebaseFirestore.getInstance()                                                  // Al pulsar aceptar se crea una partidaº con identificador local+fecha+hora y se guarda en la colecion partidas

                db.collection("Partidas").document(partida.Local + partida.Fecha + partida.Hora).update(
                    "NombreOponente", nombreee, "EmailOponente", email,"Candidatos",true).addOnSuccessListener() {
                    Log.d(TAG,"Datos actualizados")
                    db.collection("Usuarios").document(partida.Email.toString())
                        .update("Aviso",true,"Partida", "Tu partida en "+partida.Local +", dia "+ partida.Fecha +" a las " +partida.Hora+" tiene oponete").addOnSuccessListener(){
                    }
                } .addOnFailureListener() {
                    Log.d(TAG,"Error Firestore")
                }

                Toast.makeText(
                    activity,
                    "Seleccionada", Toast.LENGTH_SHORT
                ).show()
                partidasLista.clear()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        private fun spinnerProvincias(email: String?) {                                                                      //Mostrar provincias en las que haya partidas sin oponente

            val rootProvinciaRef = FirebaseFirestore.getInstance()                                                          //Obtenemos la instancia para firestore
            val provinciaRef = rootProvinciaRef.collection("Partidas")
                .whereEqualTo("Candidatos", false).whereNotEqualTo("Email",email)                           //Guardamos la consulta en una variable
            val spProvincia = binding.spProvinciaPartida                                                                    // Vinculamos al spinner del layout guardado en otra variable
            val listaProvincias: MutableList<String?> = ArrayList()                                                         //lista de provincias que resultara de la consulta a firestore
            val adapter = ArrayAdapter(                                                                                     //Adaptador del spinner con su contexto tipo de elemento y el arraylist para llenarse
                activity as Context,
                android.R.layout.simple_spinner_item,
                listaProvincias
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)                                  //Vista despliegue hacia abajo
            spProvincia.adapter = adapter
            provinciaRef.get().addOnCompleteListener(OnCompleteListener { task ->                                           //Obtenemos cada una de las provincias
                if (task.isSuccessful) {

                    listaProvincias.add("Provincia")                                         //Para que la primera opción del spinner sea buscar provincia

                    for (document in task.result) {                                         //For para ir rellenando el spinner a medida que se completen las consultas


                        val provincia = document.getString("Provincia")

                        if (listaProvincias.contains(provincia)) {                          //Para que no se repitan las provincias
                            println("ListaProvincias " + listaProvincias)                   //Para las pruebas
                        } else {

                            listaProvincias.add(provincia)                                  //La añade
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
            spProvincia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {                  //Para controlar la seleccion de una de ellas
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    provinciaSeleccion = listaProvincias[position]


                    spinnerLocalidad()                                                                      //Al seleccionar una provincia se llena el spinner localidad

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {                                   //En caso de no selecionar nada

                    Toast.makeText(activity,"Selecciona una provincia",Toast.LENGTH_SHORT).show()
                }

            }

        }


        private fun spinnerLocalidad() {
            val rootLocalidadRef = FirebaseFirestore.getInstance()
            val localidadRef = rootLocalidadRef.collection("Partidas")
                .whereEqualTo("Provincia", provinciaSeleccion).whereEqualTo("Candidatos", false)
            val spLocalidades = binding.spLocalidadPartida
            val listaLocalidades: MutableList<String?> = ArrayList()
            val adapter =
                ArrayAdapter(
                    activity as Context,
                    android.R.layout.simple_spinner_item,
                    listaLocalidades
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spLocalidades.adapter = adapter
            localidadRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {

                    listaLocalidades.add("Localidad")                                                //Para que la primera opción del spinner esté en blanco

                    for (document in task.result) {
                        val localidad = document.getString("Localidad")

                        if (listaLocalidades.contains(localidad)) {
                            println("localidad" + localidad)
                        } else {

                            listaLocalidades.add(localidad)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
            spLocalidades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    localidadSeleccion = listaLocalidades[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(activity,"Selecciona una localidad",Toast.LENGTH_SHORT).show()
                }


            }
        }




        private fun mesaXLocalidad(email: String?) {                                                    //Para rellenar el rvBuscarPartida
            val db = FirebaseFirestore.getInstance()
            db.collection("Partidas").whereEqualTo("Localidad", localidadSeleccion)         //Query con la provincia selleccionada
                .whereNotEqualTo("Email",email).whereEqualTo("Candidatos",false)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            Log.e("Error de Firestore", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {                          //for par ir rellenando

                            if (dc.type == DocumentChange.Type.ADDED) {

                                partidasLista.add(dc.document.toObject(Partida::class.java))

                            }

                        }

                        adaptadorRv.notifyDataSetChanged()
                    }
                })
        }

}
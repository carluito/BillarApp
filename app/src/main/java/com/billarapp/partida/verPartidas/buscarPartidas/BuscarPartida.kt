package com.billarapp.partida.verPartidas.buscarPartidas


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



            cargarMesas()

            spinnerProvincias()

            binding.btnBuscarPartida.setOnClickListener() {


                mesaXLocalidad(email)
                partidasLista.clear()

            }


            return binding.root


        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


        /*private fun cogerNombreOponente():String? {
            mAuth =
                FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
            val email = mAuth.currentUser?.email.toString()


            var nombre: String? = null



            db=FirebaseFirestore.getInstance()
            db.collection("Usuarios").document(email).get().addOnSuccessListener {


                nombre = (it.get("Nombre") as String?)                                                  //Para coger nombre del oponente

                println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" + nombre)


            }
            return nombre
        }*/
        private fun cargarMesas() {


            binding.rvBuscarPartida.layoutManager =                                                 //Administrador de diseño q organiza los elementos de la lista

                LinearLayoutManager(activity as Context) //context

            binding.rvBuscarPartida.setHasFixedSize(true)

            partidasLista = arrayListOf()

            adaptadorRv =

                BuscarPartidasAdapter(partidasLista) { partida -> seleccionBuscarPartida(partida ) }                 // Creamos el objeto de Nuestra clase MesasAdapter

            binding.rvBuscarPartida.adapter = adaptadorRv


        }


        private fun seleccionBuscarPartida(partida: Partida) {
            mAuth =
                FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
            val email = mAuth.currentUser?.email.toString()


            var nombreee: String? = null



            db=FirebaseFirestore.getInstance()
            db.collection("Usuarios").document(email).get().addOnSuccessListener {


                nombreee =
                    (it.get("Nombre") as String?)                                                  //Para coger nombre del oponente
            }

                println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" + nombreee)

            val builder = AlertDialog.Builder(activity as Context)
            builder.setTitle("¿Jugar?")
            builder.setMessage("¿Quieres jugar esta partida?")

            builder.setPositiveButton("Cancelar") { _, _ ->
                Toast.makeText(
                    activity,
                    "Espera a que confirme tu contrincante", Toast.LENGTH_SHORT
                ).show()

            }
            builder.setNegativeButton("Aceptar") { _, _ ->

                db=FirebaseFirestore.getInstance()

                db.collection("Partidas").document(partida.Local + partida.Fecha + partida.Hora).update(
                    "Candidatos", nombreee, "EmailOponente", email).addOnSuccessListener() {
                    println("siiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                } .addOnFailureListener() {
                    println("noooooooooooooooooooooooooooooooooooo")
                }
                println("sssssssssssssssssssssssssssssssssssssssssssss$nombreee")

                partidasLista.clear()

                Toast.makeText(
                    activity,
                    "Espera a que confirme tu contrincante", Toast.LENGTH_SHORT
                ).show()
            }


            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
     /*   private fun cambiarNombreeEmailOponente(nombree: String, partida: Partida){

println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+nombree)

            mAuth = FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
            val email = mAuth.currentUser?.email.toString()
            db=FirebaseFirestore.getInstance()

            db.collection("Partidas").document(partida.Local + partida.Fecha + partida.Hora).update(
                "Candidatos", nombree, "EmailOponente", email).addOnSuccessListener() {
                    println("siiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                } .addOnFailureListener() {
                    println("noooooooooooooooooooooooooooooooooooo")
                }

        }*/


        private fun spinnerProvincias() {

            val rootProvinciaRef = FirebaseFirestore.getInstance()
            val provinciaRef = rootProvinciaRef.collection("Partidas")
            val spProvincia = binding.spProvinciaPartida
            val listaProvincias: MutableList<String?> = ArrayList()
            val adapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_spinner_item,
                listaProvincias
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spProvincia.adapter = adapter
            provinciaRef.get().addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {

                    listaProvincias.add("Provincia")                                         //Para que la primera opción del spinner sea buscar provincia

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
                    Toast.makeText(activity,"Selecciona una provincia",Toast.LENGTH_SHORT).show()
                }

            }

        }

        private fun spinnerLocalidad() {
            val rootLocalidadRef = FirebaseFirestore.getInstance()
            val localidadRef = rootLocalidadRef.collection("Partidas")
                .whereEqualTo("Provincia", provinciaSeleccion)
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




        private fun mesaXLocalidad(email: String?) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Partidas").whereEqualTo("Localidad", localidadSeleccion).whereNotEqualTo("Email",email).whereEqualTo("Candidatos","Candidatos")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

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

                                partidasLista.add(dc.document.toObject(Partida::class.java))

                            }

                        }

                        adaptadorRv.notifyDataSetChanged()
                    }
                })
        }



}
package com.billarapp.partida.verPartidas


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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.FragmentBuscarPartidaBinding
import com.billarapp.mesasBillar.Mesa
import com.billarapp.partida.verPartidas.adapterBuscarPartidas.BuscarPartidasAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*

class BuscarPartida : Fragment() {

    private lateinit var partidasLista: ArrayList<Partida>
    private  var _binding: FragmentBuscarPartidaBinding?=null
    private val binding get()= _binding!!
     private lateinit var adaptadorRv: BuscarPartidasAdapter
    private var provinciaSeleccion: String?=null
    private var localidadSeleccion: String?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding=FragmentBuscarPartidaBinding.inflate(inflater, container, false)

        cargarMesas()
        spinnerProvincias()

        binding.btnBuscarPartida.setOnClickListener(){


            mesaXLocalidad()

        }


        return binding.root



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



   private fun cargarMesas(){

                                                                                                            //Administrador de diseño q organiza los elementos de la lista

        binding.rvBuscarPartida.layoutManager = LinearLayoutManager(activity as Context) //context

       binding.rvBuscarPartida.setHasFixedSize(true)



        partidasLista = arrayListOf()

        adaptadorRv = BuscarPartidasAdapter(partidasLista) { partida -> seleccionBuscarPartida(partida) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvBuscarPartida.adapter = adaptadorRv



    }
    private fun seleccionBuscarPartida(partida: Partida){
        Toast.makeText(activity,"Partida Publicada", Toast.LENGTH_SHORT).show()
    }
    private fun spinnerProvincias(){

        val rootProvinciaRef = FirebaseFirestore.getInstance()
        val provinciaRef = rootProvinciaRef.collection("Partidas")
        val spProvincia= binding.spProvinciaPartida
        val listaProvincias: MutableList<String?> = ArrayList()
        val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_item, listaProvincias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvincia.adapter = adapter
        provinciaRef.get().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                listaProvincias.add("Provincia")                                         //Para que la primera opción del spinner sea buscar todas

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


                 spinnerLocalidad()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Nada seleccionado")
            }

        }

    }

    private fun spinnerLocalidad(){
        val rootLocalidadRef = FirebaseFirestore.getInstance()
        val localidadRef = rootLocalidadRef.collection("Partidas").whereEqualTo("Provincia",provinciaSeleccion)
        val spLocalidades= binding.spLocalidadPartida
        val listaLocalidades: MutableList<String?> = ArrayList()
        val adapter =
            ArrayAdapter(activity as Context, android.R.layout.simple_spinner_item, listaLocalidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLocalidades.adapter = adapter
        localidadRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
            if (task.isSuccessful) {

                listaLocalidades.add("Localidad")                                                //Para que la primera opción del spinner esté en blanco

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
                println(" Nada seleccionado")
            }


        }
    }

    private fun todasPartidas() {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor

        val db = FirebaseFirestore.getInstance()
        db.collection("Partidas").
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

                        partidasLista.add(dc.document.toObject(Partida::class.java))

                    }
                }

                adaptadorRv.notifyDataSetChanged()

            }
        })

    }



    private fun mesaXLocalidad(){
        val db=FirebaseFirestore.getInstance()
        db.collection("Partidas").whereEqualTo("Localidad",localidadSeleccion).addSnapshotListener(object:EventListener<QuerySnapshot>{

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
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

package com.billarapp.partida.verPartidas.pendientes

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.R
import com.billarapp.databinding.FragmentBuscarPartidaBinding
import com.billarapp.databinding.FragmentPendientesBinding
import com.billarapp.partida.verPartidas.Partida
import com.billarapp.partida.verPartidas.buscarPartidas.BuscarPartidasAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Pendientes : Fragment() {

    private lateinit var partidasLista: ArrayList<Partida>
    private var _binding: FragmentPendientesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adaptadorRv: PendientesAdapter
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPendientesBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()

        val email=mAuth.currentUser?.email.toString()

        cargarMesas()

        todasPartidas(email)

        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun cargarMesas() {

        binding.rvPendientes.layoutManager =                                                    //Administrador de diseño q organiza los elementos de la lista
            LinearLayoutManager(activity as Context) //context

        binding.rvPendientes.setHasFixedSize(true)



        partidasLista = arrayListOf()

        adaptadorRv =
            PendientesAdapter(partidasLista) { partida -> seleccionPendientes(partida) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvPendientes.adapter = adaptadorRv


    }
    private fun seleccionPendientes(partida: Partida) {
        Toast.makeText(activity, "Partida Publicada", Toast.LENGTH_SHORT).show()
    }

    private fun todasPartidas(email:String?) {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor

        val db = FirebaseFirestore.getInstance()
        db.collection("Partidas").whereEqualTo("Email",email).addSnapshotListener(object :
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

                        partidasLista.add(dc.document.toObject(Partida::class.java))

                    }
                }

                adaptadorRv.notifyDataSetChanged()

            }
        })

    }
}



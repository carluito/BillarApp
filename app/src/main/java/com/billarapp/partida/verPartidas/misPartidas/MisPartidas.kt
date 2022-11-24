package com.billarapp.partida.verPartidas.misPartidas

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.databinding.FragmentMisPartidasBinding
import com.billarapp.partida.verPartidas.Partida
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.sql.Timestamp
import java.time.Instant.now
import java.time.LocalDate.now
import java.util.*
import kotlin.collections.ArrayList

class MisPartidas : Fragment() {

    private lateinit var partidasLista: ArrayList<Partida>
    private var _binding: FragmentMisPartidasBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorRvM: MisPartidasAdapter
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPartidasBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        mAuth =
            FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta mediante currentUser
        val email = mAuth.currentUser?.email.toString()


        cargarMesas()
        misPartidasPublicadas(email)

        binding.btnPublicadas.setOnClickListener(){
            partidasLista.clear()
            misPartidasPublicadas(email)
        }
        binding.btnApuntado.setOnClickListener(){
            partidasLista.clear()
            misPartidasComoOponente(email)
        }
        binding.btnJugadas.setOnClickListener(){
            partidasLista.clear()
            Jugada(email)
        }


        return binding.root
    }

    private fun cargarMesas() {


        binding.rvMisPartidas.layoutManager =
            LinearLayoutManager(activity as Context)          //Administrador de diseño q organiza los elementos de la lista

        binding.rvMisPartidas.setHasFixedSize(true)



        partidasLista = arrayListOf()

        adaptadorRvM =
            MisPartidasAdapter(partidasLista) { partida -> miasSelected(partida) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvMisPartidas.adapter = adaptadorRvM


    }

    private fun miasSelected(partida: Partida) {                                                                              //Para gestionar la selección de items, pero solo es visualización


    }

    private fun misPartidasPublicadas(email: String?) {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor
        val db = FirebaseFirestore.getInstance()


        db.collection("Partidas").whereEqualTo("Email",email).whereEqualTo("Jugada" ,false).whereEqualTo("Candidatos",true)

            .addSnapshotListener(object :
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

                            adaptadorRvM.notifyDataSetChanged()

                        }
                    })



    }

    private fun misPartidasComoOponente(email: String?) {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor
        val db = FirebaseFirestore.getInstance()



        db.collection("Partidas").whereEqualTo("EmailOponente",email).whereEqualTo("Jugada" ,false)
            .addSnapshotListener(object :
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

                    adaptadorRvM.notifyDataSetChanged()

                }
            })



    }

    private fun Jugada(email: String?) {

        val db = FirebaseFirestore.getInstance()



        db.collection("Partidas").whereEqualTo("Email",email).whereEqualTo("EmailOponente",email).                      //Consulta para llenar el recycler view donde el email de usuario coincida con el email  y emailoponente ademas de q jugada sea true
        whereEqualTo("Jugada",true).addSnapshotListener(object :

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

                    adaptadorRvM.notifyDataSetChanged()

                }
            })

    }

}
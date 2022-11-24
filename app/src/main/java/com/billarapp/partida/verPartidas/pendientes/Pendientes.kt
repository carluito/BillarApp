package com.billarapp.partida.verPartidas.pendientes

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var adaptadorRvp: PendientesAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPendientesBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()

        val email=mAuth.currentUser?.email.toString()

        cargarMesas()

        partidasPendientes(email)

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

        adaptadorRvp =
            PendientesAdapter(partidasLista) { partida -> seleccionPendientes(partida) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvPendientes.adapter = adaptadorRvp


    }
    private fun seleccionPendientes(partida: Partida) {
        alertDialog(partida)
    }

    private fun partidasPendientes(email:String?) {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor

        val db = FirebaseFirestore.getInstance()
        db.collection("Partidas").whereEqualTo("Email",email).whereEqualTo("Candidatos",false).
        whereGreaterThan("FechaHora" ,com.google.firebase.Timestamp.now()).addSnapshotListener(object :
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

                adaptadorRvp.notifyDataSetChanged()

            }
        })

    }
    private fun alertDialog(partida: Partida){

        val builder = AlertDialog.Builder(activity as Context)                                  //Alert dialog con dos botones para confirmar
        builder.setTitle("¿Borrar?")
        builder.setMessage("¿Quieres borrar esta partida?")

        builder.setPositiveButton("Cancelar") { _, _ ->

        }
        builder.setNegativeButton("Aceptar") { _, _ ->

            db=FirebaseFirestore.getInstance()                                                  // Al pulsar aceptar se crea una partida con identificador local+fecha+hora y se guarda en la colección Partidas

            db.collection("Partidas").document(partida.Local + partida.Fecha + partida.Hora).delete().addOnSuccessListener() {
                Log.d(ContentValues.TAG,"Datos actualizados")
            } .addOnFailureListener() {
                Log.d(ContentValues.TAG,"Error Firestore")
            }

            Toast.makeText(
                activity,
                "Borrada", Toast.LENGTH_SHORT
            ).show()
            partidasLista.clear()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()






    }
}



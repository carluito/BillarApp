package com.billarapp.partida.verPartidas.misPartidas

import android.content.Context
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.billarapp.R
import com.billarapp.databinding.FragmentMisPartidasBinding
import com.billarapp.partida.verPartidas.Partida
import com.billarapp.partida.verPartidas.VerPartidas
import com.billarapp.partida.verPartidas.ViewPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MisPartidas : Fragment() {

    private lateinit var partidasLista: ArrayList<Partida>
    private lateinit var _binding: FragmentMisPartidasBinding
    private val binding get() = _binding
    private lateinit var adaptadorRv: MisPartidasAdapter
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPartidasBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        mAuth =
            FirebaseAuth.getInstance()                                                  //Para saber el email del usuario x si se apunta
        val email = mAuth.currentUser?.email.toString()


        cargarMesas()

        misPartidas(email)


        return binding.root
    }

    private fun cargarMesas() {


        binding.rvMisPartidas.layoutManager =
            LinearLayoutManager(activity as Context)          //Administrador de diseño q organiza los elementos de la lista

        binding.rvMisPartidas.setHasFixedSize(true)



        partidasLista = arrayListOf()

        adaptadorRv =
            MisPartidasAdapter(partidasLista) { partida -> miasSelected(partida) }                                             // Creamos el objeto de Nuestra clase MesasAdapter

        binding.rvMisPartidas.adapter = adaptadorRv


    }

    private fun miasSelected(partida: Partida) {

    }

    private fun misPartidas(email: String?) {                                                                      //Método para ver todas las mesas, a ver si encuentro un metodo mejor
        val db = FirebaseFirestore.getInstance()



                db.collection("Partidas").whereNotEqualTo("Candidatos", "Candidatos").whereEqualTo("Email",email).whereEqualTo("EmailOponente",email)
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

                            adaptadorRv.notifyDataSetChanged()

                        }
                    })



    }

}
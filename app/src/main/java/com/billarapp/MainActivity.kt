package com.billarapp

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import com.billarapp.databinding.ActivityMainBinding
import com.billarapp.mesasBillar.BuscarMesa
import com.billarapp.partida.verPartidas.VerPartidas
import com.billarapp.partida.verPartidas.misPartidas.MisPartidas
import com.billarapp.registroDatos.UneteRegistro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.lang.reflect.Array
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var bindingMain: ActivityMainBinding                                   //Modificador Private para que solo sea visible en la clase actual y lateinit
                                                                                            //para iniciarla por fuera del constructor con seguridad en caso de que sea no anulable
    private lateinit var intentMain : Intent
    private lateinit var mAuth:FirebaseAuth                                                //Para inicializar la variable q nos dará el email

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        bindingMain.btnAviso.visibility= View.GONE                                        //Oculta el image view ivAviso
        revisarAviso(pillarEmail())
        actualizarPartidas(pillarEmail())
        miCuenta()
        buscador()
        info()


    }

    private fun miCuenta(){


    bindingMain.btnUnirse.setOnClickListener{

        intentMain = Intent(this, UneteRegistro::class.java)
        startActivity(intentMain)
        Toast.makeText(this, "Unete para echar una partida", Toast.LENGTH_SHORT).show()

    }
}

    private fun buscador(){
        bindingMain.btnMesas.setOnClickListener{

            intentMain = Intent (this, BuscarMesa::class.java)
            startActivity(intentMain)
            Toast.makeText(this,"Descubre dónde puedes jugar", Toast.LENGTH_SHORT).show()

        }

    }
    private fun info(){
        bindingMain.btnInfo.setOnClickListener{ Toast.makeText(this,"Gracias!!.Contacta en billarappcontact@gmail.com", Toast.LENGTH_LONG).show()}
    }

    private fun pillarEmail(): String {                                             //Para saber el email del usuario para actualizar
        mAuth =
            FirebaseAuth.getInstance()
        val email = mAuth.currentUser?.email.toString()
        return email
   }
    private fun actualizarPartidas(email:String?){                                  //Para actualizar la coleccion Partidas borrando las pasadas sin oponente

            val db = FirebaseFirestore.getInstance()
            val codeRef = db.collection("Partidas")


            codeRef.whereLessThan("FechaHora", com.google.firebase.Timestamp.now()).whereEqualTo("Candidatos", false)
                .whereEqualTo("Email", email).get()                                                                //Primero obtenemos las partidas que son el pasado
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            codeRef.document(document.id).delete()                                                      //La borramos
                        }
                    }
                }

            codeRef.whereLessThan("FechaHora",com.google.firebase.Timestamp.now()).whereEqualTo("Candidatos",true)   //Primero obtenemos las partidas que son el pasado

                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val update: MutableMap<String, Any> = HashMap()
                            update["Jugada"] = true
                            codeRef.document(document.id).set(update, SetOptions.merge())
                        }
                    }
                }
    }

    private fun pulsarAviso(email: String){                                     //Para que al pulsar el botón del aviso el campo Aviso vuelva a false
        val db = FirebaseFirestore.getInstance()

      var nombrePartida:String?= null

         db.collection("Usuarios").document(email).get().addOnSuccessListener(){
             nombrePartida=(it.get("Partida")as String?)
         }
        var msg="Tu partida "+nombrePartida+ " tiene oponente"
        bindingMain.btnAviso.setOnClickListener(){


            Toast.makeText(this, nombrePartida, Toast.LENGTH_LONG).show()

            db.collection("Usuarios").document(email).update("Aviso",false).addOnSuccessListener(){         //Aqui le damos el valor false
            }
            intentMain = Intent (this, VerPartidas::class.java)                                                        //Intent para ir a la activity de ver partidas
            startActivity(intentMain)
        }

    }
    private fun revisarAviso(email: String?){                                                                                      //función para saber si hay oponente
        val db = FirebaseFirestore.getInstance()
        val codeRef = email?.let { db.collection("Usuarios").document(it) }
        var aviso:Boolean?
        if (codeRef != null) {
            codeRef.get().addOnSuccessListener(){
                aviso =(it.get("Aviso") as Boolean?)
                if(aviso==true){
                    bindingMain.btnAviso.visibility= View.VISIBLE
                    val animator = ObjectAnimator.ofInt(bindingMain.btnAviso,"backgroundColor", Color.WHITE, Color.RED)
                    animator.setDuration(500)
                    animator.setEvaluator(ArgbEvaluator())
                    animator.repeatCount= Animation.REVERSE
                    animator.repeatCount= Animation.INFINITE
                    animator.start()
                    pulsarAviso(pillarEmail())
                }
                else{
                    bindingMain.btnAviso.visibility= View.GONE
                }
            }

        }

    }
}
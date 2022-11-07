package com.billarapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.billarapp.partida.crearPartida.CrearPartida
import com.billarapp.databinding.ActivityUsuarioBinding
import com.billarapp.partida.verPartidas.Partida
import com.billarapp.partida.verPartidas.VerPartidas
import com.billarapp.registroDatos.EditarUsuario
import com.google.android.gms.common.internal.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class Usuario : AppCompatActivity() {

    private lateinit var intentUsuario: Intent
    private lateinit var bindingUsuario: ActivityUsuarioBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUsuario = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(bindingUsuario.root)

        val email = traeEmail()
        val proveedor = traerProveedor()

        sesion(email,proveedor)
        editar(email?:"email",proveedor?:"proveedor")
        crearPartida(email,proveedor)
        cerrarSesion()

        bindingUsuario.btnVerPartidas.setOnClickListener(){

            partidasJugadas(email)
            intentUsuario=Intent(this,VerPartidas::class.java).apply {

            }
            startActivity(intentUsuario)
        }
    }

    override fun onBackPressed() {                              //Sobreescribimos el método onBackPressed para q al pulsar el boton retroceso vuelva a MainActivity
        super.onBackPressed()
        intentUsuario= Intent(this,MainActivity::class.java)
        startActivity((intentUsuario))
    }


    private fun cerrarSesion() {

        bindingUsuario.btnCerrarSesion.setOnClickListener {

            val preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE).edit()                  //Para borrar la contraseña de sharedPreferences
            preferencias.clear()
            preferencias.apply()

            FirebaseAuth.getInstance().signOut()                                                                        //y cerrar sesión
            intentUsuario = Intent(this, MainActivity::class.java)
            startActivity(intentUsuario)

        }
    }


    private fun traeEmail(): String? {

        val paqueteUnete: Bundle? = intent.extras                                        //Para pasar los datos de email y provedor pero solo pintamos el email en el etEmailUsuario
        val email: String? = paqueteUnete?.getString("email")

        return email
    }

    private fun traerProveedor(): String?{
        val paqueteUnete2 : Bundle? =  intent.extras
        val proveedor: String?= paqueteUnete2?.getString("proveedor")
        bindingUsuario.tvProveedorUsuario.text= proveedor

        return proveedor
    }

    private fun editar(email: String?,proveedor: String?){                                                  //Para modificar los datos de los usuarios
        bindingUsuario.btnEditarDatos.setOnClickListener {

            intentUsuario= Intent(this, EditarUsuario::class.java).apply {

                putExtra("email",email)
                putExtra("proveedor",proveedor )
            }

            startActivity(intentUsuario)
        }
    }

    private fun sesion(email: String?,proveedor: String?){
        val preferencias = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE).edit()                                                 //Para guardar la sesión
       preferencias.putString("email", email)
       preferencias.putString("proveedor",proveedor)
       preferencias.apply()

    }

    private fun crearPartida(email:String?,proveedor: String?){


        bindingUsuario.btnCrearPartida.setOnClickListener(){

            intentUsuario=Intent(this, CrearPartida::class.java).apply {
                putExtra("email",email)
                putExtra("proveedor",proveedor)
            }
            startActivity(intentUsuario)

        }
    }

   private fun partidasJugadas(email:String?) {                                 //Método para saber q partida es el pasado combinando una consulta y un update

       val db = FirebaseFirestore.getInstance()
       val codeRef = db.collection("Partidas")


       codeRef.whereLessThan("FechaHora",com.google.firebase.Timestamp.now()).whereEqualTo("Email",email)   //Primero obtenemos las partidas que son el pasado
           .whereEqualTo("EmailOponente",email)
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
}
package com.billarapp.registroDatos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.billarapp.MainActivity
import com.billarapp.R
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityUneteRegistroBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class UneteRegistro : AppCompatActivity() {

    private val GOOGLESIGNIN = 100
    private lateinit var intentUnete: Intent
    private lateinit var bindingUneteRegistro: ActivityUneteRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUneteRegistro = ActivityUneteRegistroBinding.inflate(layoutInflater)
        setContentView(bindingUneteRegistro.root)




        registro()
        sesion()




        bindingUneteRegistro.btnVolverRegistro.setOnClickListener {
            intentUnete = Intent(this, MainActivity::class.java)
            startActivity(intentUnete)

        }
    }





    private fun sesion() {                                                           //Metodo para guardar el usuario y la contraseña para no tener q meterla hasta q cierre sesión

        val preferencias = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE)
        val email: String? = preferencias.getString("email", null)
        val proveedor: String? = preferencias.getString("proveedor", null)

        if (email != null && proveedor != null) {

            /*aCompletarFicha(email, tipoProveedor.valueOf(proveedor)) //Para completaR Los datos del jugador y hacer la base de datos de jugadores*/

            intentUnete = Intent(this, Usuario::class.java).apply {

                putExtra("email", email)
                putExtra("proveedor", proveedor)
            }
            startActivity(intentUnete)


        }
    }

    private fun registro() {
        val email = bindingUneteRegistro.etEmailRegistro
        val senha = bindingUneteRegistro.etSenhaRegistro


        bindingUneteRegistro.btnRegistro.setOnClickListener {


            if (email.text.isNotEmpty() || Patterns.EMAIL_ADDRESS.matcher(email.toString())
                    .matches() || senha.text.isNotEmpty()
            ) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), senha.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            aCompletarFicha(
                                it.result?.user?.email ?: "",
                                tipoProveedor.EMAIL)              //Enviamos el email y el tipo de proveedor a completar los datos
                            // println(it.result?.user?.email?:"")
                        } else {
                            fallo()
                        }
                    }
            } else {
                Toast.makeText(this, "Me cago en dios pon bien el santo y seña", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        bindingUneteRegistro.btnAcceso.setOnClickListener {                  //Lo tengo asi para acceder desde el  mi cuenta, un lio,pero creo q ya bien
            if (email.text.isNotEmpty() && senha.text.isNotEmpty()) {


                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(), senha.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            yaRegistrado(it.result?.user?.email ?: "",tipoProveedor.EMAIL)//TODO
                        } else {
                            fallo()
                        }
                    }

            }/*else{
                Toast.makeText(this,"Me cago en dios pon santo y seña",Toast.LENGTH_SHORT).show()}*/
        }
        bindingUneteRegistro.btnGoogle.setOnClickListener {

            //Configuración para entrar con cuenta google

            val configuracionGoogle =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val clienteGoogle = GoogleSignIn.getClient(this, configuracionGoogle)
            clienteGoogle.signOut()

            startActivityForResult(clienteGoogle.signInIntent, GOOGLESIGNIN)

        }
    }

    private fun fallo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Fallo al autenticar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun aCompletarFicha(email: String?, proveedor: tipoProveedor) {

        intentUnete = Intent(
            this,
            DatosJugador::class.java
        ).apply {                            //Para datos jugador q será la definitiva, una vez se registre pasamos a datosJugador para rellenar datos

            putExtra("email", email)
            putExtra("proveedor", proveedor.name)
        }
        startActivity(intentUnete)

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLESIGNIN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)

                if (cuenta != null) {

                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                cuenta.toString()
                                yaRegistrado(cuenta.email ?: "", tipoProveedor.GOOGLE)
                            } else {
                                fallo()
                            }
                        }
                }
            } catch (e: ApiException) {
                fallo()

            }
        }
    }

    private fun yaRegistrado(email: String?, proveedor: tipoProveedor) {

        val db = FirebaseFirestore.getInstance()
        db.collection("Usuarios").document(email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    intentUnete = Intent(this, Usuario::class.java).apply {

                        putExtra("email", email)
                        putExtra("proveedor", proveedor.name)
                    }
                    startActivity(intentUnete)


                } else {
                    aCompletarFicha(email, proveedor)
                }
            }
    }


}
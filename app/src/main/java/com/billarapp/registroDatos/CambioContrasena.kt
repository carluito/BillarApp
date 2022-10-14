package com.billarapp.registroDatos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.billarapp.R
import com.billarapp.databinding.FragmentCambioContrasenaBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class CambioContrasena : Fragment(R.layout.fragment_cambio_contrasena) {

    private var _binding: FragmentCambioContrasenaBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCambioContrasenaBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()

       /* val miPaquete =
            arguments                                         //Para recibir el email enviado desde el activity EditarUsuario
        val email = miPaquete!!.getString("email")*/

        binding.btnCambio.setOnClickListener() {

            cambioPassword()
        }

        return binding.root//inflater.inflate(R.layout.fragment_cambio_contrasena,container,false)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cambioPassword() {                                                                              //Método para cambiar el password
        if (binding.etContrasenaOld.text.toString()
                .isNotEmpty() && binding.etContrasenaNew.text.toString()
                .isNotEmpty() && binding.etContrasenaRepeat.text.toString().isNotEmpty()
        ) {

            if (binding.etContrasenaNew.text.toString() == binding.etContrasenaRepeat.text.toString()) {

                val user = mAuth.currentUser
                if ((user != null) && (user.email != null)) {
                    val credencial: AuthCredential = EmailAuthProvider.getCredential(
                        user.email!!,
                        binding.etContrasenaOld.text.toString()
                    )
                    user.reauthenticate(credencial)                                                                     //Para comprobar el password anterior
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                //Toast.makeText(activity,"Hecho",Toast.LENGTH_SHORT).show()
                                user.updatePassword(binding.etContrasenaNew.text.toString())                            //Para establecer el nuevo password
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                activity,
                                                "Password actualizado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            //mAuth.signOut()


                                        } else {
                                            println("oooooooo")
                                        }
                                    }
                            } else {
                                Toast.makeText(activity, "No es tu contraseña actual", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(activity, "Usuario nulo", Toast.LENGTH_SHORT).show()

                }

            } else {
                Toast.makeText(activity, "No coinciden", Toast.LENGTH_SHORT).show()
            }
        } else {

            Toast.makeText(activity, "Rellena todos los campos", Toast.LENGTH_SHORT).show()

        }
    }
   /* fun Fragment.hideKeyboard() {                                                                                     //Para esconder el teclado pero hay q mirarlo bien
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }*/
}
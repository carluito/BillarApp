package com.billarapp.registroDatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.billarapp.R
import com.billarapp.Usuario
import com.billarapp.databinding.ActivityEditarUsuarioBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditarUsuario : AppCompatActivity() {


    private lateinit var bindingEditar: ActivityEditarUsuarioBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var intentEditar: Intent

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bindingEditar=ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(bindingEditar.root)

        val paqueteEdit :Bundle? = intent.extras
        val email :String? = paqueteEdit?.getString("email")
        pintaDatos(email)
        spProvincias()
        spDisponibilidad()
        spNivel()


        bindingEditar.btEditar.setOnClickListener{

            update(email)

        }

        volverse()

    }





    private fun pintaDatos(email: String?){

        db=FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("Usuarios").document(email).get().addOnSuccessListener {
                bindingEditar.tx1.setText(it.get("Nombre")as String?)
                bindingEditar.tx3.setText(it.get("Localidad")as String?)
                bindingEditar.tx2.setText(it.get("Provincia")as String?)
                bindingEditar.tx4.setText(it.get("Nivel")as String?)
                bindingEditar.tx5.setText(it.get("Disponibilidad")as String?)

            }

        }
    }







    private fun spProvincias(){

        val provinciaSp = bindingEditar.spProvincia2
        val listaprovincias= resources.getStringArray(R.array.provinciaDJ)
        val adaptadorProvincias: ArrayAdapter<*> =
            ArrayAdapter(this,android.R.layout.simple_spinner_item, listaprovincias)
        provinciaSp.adapter= adaptadorProvincias
        provinciaSp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, posicion: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO saber q hacer para q guarde tos los datos del jugador
            }
        }
    }





    private fun spNivel() {

        val nivelSp = bindingEditar.spNivel2
        val listaNivel = resources.getStringArray(R.array.nivel)
        val adaptadorNivel: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listaNivel)
        nivelSp.adapter = adaptadorNivel

        nivelSp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                p0: AdapterView<*>,
                vista: View,
                posicion: Int,
                id: Long
            ) {


            }
            override fun onNothingSelected(parent: AdapterView<*>) {


            }

        }
    }





    private fun spDisponibilidad(){
        val disponibilidadSp = bindingEditar.spDisponibilidad2
        val listaDisponibilida = resources.getStringArray(R.array.disponibilidad)
        val adaptadorDisponibilida: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaDisponibilida)
        disponibilidadSp.adapter = adaptadorDisponibilida

        disponibilidadSp.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO saber q hacer para q guarde tos los datos del jugador
            }


        }

    }






    private fun update(email :String?){

        val uNombre = bindingEditar.etNombre2.text.trim().toString()
        val uLocalidad= bindingEditar.etLocalidad2.text.trim().toString()
        val uNivel= bindingEditar.spNivel2.selectedItem.toString()
        val uProvincia= bindingEditar.spProvincia2.selectedItem.toString()
        val uDisponibilidad= bindingEditar.spDisponibilidad2.selectedItem.toString()

        db= FirebaseFirestore.getInstance()
        if (email != null) {
            if (uNombre.isNotEmpty()) {
                db.collection("Usuarios").document(email)
                    .update("Nombre", uNombre)
            }
            if(uLocalidad.isNotEmpty()){
                db.collection("Usuarios").document(email).update("Localidad", uLocalidad)
            }
            if(uNivel!=""){
                db.collection("Usuarios").document(email).update("Nivel", uNivel)
            }
            if(uProvincia != ""){
                db.collection("Usuarios").document(email).update("Provincia", uProvincia)
            }
            if(uDisponibilidad != ""){
                db.collection("Usuarios").document(email).update("Disponibilidad", uDisponibilidad)
            }
        }
        pintaDatos(email)

    }




    private fun volverse(){

        bindingEditar.btnVolverEditar.setOnClickListener {

            intentEditar = Intent(this, Usuario::class.java)
            startActivity(intentEditar)
        }
    }
}
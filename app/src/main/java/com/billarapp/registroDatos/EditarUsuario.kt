package com.billarapp.registroDatos


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.billarapp.MainActivity
import com.billarapp.R
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
        val proveedor:String?=paqueteEdit?.getString("proveedor")


        if (proveedor!="EMAIL"){
                        bindingEditar.btnCambioContrasena.visibility=View.GONE                                        //Oculta el boton al acceder medante google ya q la contraseña es la de la cuenta google
                }


        pintaDatos(email)
        spProvincias()
        spDisponibilidad()
        spNivel()

        bindingEditar.btEditar.setOnClickListener{

            update(email)

        }


        bindingEditar.btnBorrarUsuario.setOnClickListener(){

            borrarCuenta(email)

        }

        bindingEditar.btnCambioContrasena.setOnClickListener(){

            val miFragmento= CambioContrasena()
            val fragmento: Fragment?=
                supportFragmentManager.findFragmentByTag(CambioContrasena::class.java.simpleName)

            if (fragmento !is Fragment){

                val miPaquete = Bundle()
                miPaquete.putString("email",email)                          //Para enviar el email a los fragments de esta actividad
                miFragmento.arguments=miPaquete

                supportFragmentManager.beginTransaction()
                    .add(R.id.mostrarFragments,miFragmento,CambioContrasena::class.java.simpleName)
                    .addToBackStack(null)                                                           //Hace q cuando pulsamos atras desaparezca el fragmento
                    .commit()
            }
           // bindingEditar.btnCambioContrasena.visibility=View.GONE                                        //Oculta el boton
        }


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






    private fun borrarCuenta(email: String?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Borrar Cuenta")
        builder.setMessage("Se borrarán todos los datos")
        builder.setPositiveButton("Cancelar"){ _, _ ->
            Toast.makeText(this,
                "Que susto¡¡¡, gracias", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Aceptar"){ _, _ ->
            Toast.makeText(this,
                "Vuelve Pronto, te echaré de menos", Toast.LENGTH_SHORT).show()
            if (email != null) {                                                                        //Para borrar los datos de la cuenta
                db.collection("Usuarios").document(email).delete()
                intentEditar = Intent(this, MainActivity::class.java)
                startActivity(intentEditar)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
package com.billarapp.partida.crearPartida.adapterCrearParitda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewMesasItemPartidaBinding
import com.billarapp.mesasBillar.Mesa
import java.util.concurrent.Executors

class PartidaHolder(private val binding: ViewMesasItemPartidaBinding) : RecyclerView.ViewHolder(binding.root) { //Para definir el objeto contendor de vistas q define cada elemento

    fun bind(mesa: Mesa) {

        mesa.Foto?.let { mostrarUrl(it) }                       //!!!no se si estara bien originalmente yo puse   mostrarUrl(mesa.Foto)
        binding.txSitio.text = mesa.Local
        binding.txDireccion.text = mesa.Calle


        itemView.setOnClickListener() {                           // Para q al clickar en un item del recyclerview seleccione ese local para la partida

            val builder = AlertDialog.Builder(binding.ivSitio.context)
            builder.setTitle("Partida")
            builder.setMessage("Lugar escogido: " + mesa.Local)
            builder.setPositiveButton("Cancelar",null)
            builder.setNegativeButton("Aceptar") { _, _ ->
                Toast.makeText(
                    binding.ivSitio.context,
                    "Partida Publicada", Toast.LENGTH_SHORT
                ).show()

            }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        }
    }


private fun mostrarUrl(Foto: String) {                        //Metodo para poner la url en el recyclerView

        val imageView = binding.ivSitio                     //Declaración del imageView

        val executor = Executors.newSingleThreadExecutor()      //Declaración Executor para analizar la Url

        val handler = Handler(Looper.getMainLooper())           //Handler se encarga de cargar la imagen una vez analizada proporcionando
        //una interfaz para enviar trabajo a los hilos de looper

        var imagen: Bitmap?                               //Iniciar la imagen

        executor.execute {                                      // Proceso en segundo plano, depende de la velocidad de conexión

            try {                                               //Try & catch para poner la imagen en el imageView con el Handler
                val `in` = java.net.URL(Foto).openStream()
                imagen = BitmapFactory.decodeStream(`in`)


                handler.post {                                  //Sólo para hacer cambiosen la imgen
                    imageView.setImageBitmap(imagen)
                }
            }


            catch (e: Exception) {                              //Por si la Url no apunta a una imagen u otro tipo de fallo
                e.printStackTrace()
            }
        }
    }
}

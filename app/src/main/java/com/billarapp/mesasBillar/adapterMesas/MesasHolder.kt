package com.billarapp.mesasBillar.adapterMesas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.billarapp.databinding.ViewMesasItemBinding
import com.billarapp.mesasBillar.Mesa
import java.util.concurrent.Executors

class MesasHolder(private val binding: ViewMesasItemBinding) : RecyclerView.ViewHolder(binding.root) { //Para definir el objeto contendor de vistas q define cada elemento

    fun bind(mesa: Mesa, onClickListener: (Mesa)->Unit) {

        mesa.Foto?.let { mostrarUrl(it) }                       //!!!no se si estara bien originalmente yo puse   mostrarUrl(mesa.Foto)
        binding.txSitio.text = mesa.Local
        binding.txProvincia.text = mesa.Provincia
        binding.txLocalidad.text = mesa.Localidad
        binding.txDireccion.text = mesa.Calle

        itemView.setOnLongClickListener{onClickListener(mesa)
        return@setOnLongClickListener true                              //setOnLongClickListener requiere devolver un valor booleano para notificar si se consumió el evento
        }

    } private fun mostrarUrl(Foto: String) {                        //Metodo para poner la url en el recyclerView

        val imageView = binding.ivSitio                       //Declaración del imageView

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

package com.billarapp.partida.crearPartida

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class PonerFechaFragment(val listener : (dia:Int, mes:Int, anho:Int) -> Unit):  //Un listener q cuando lo ejecutemos le tendremos q pasar estos valores q extiende de la clase Dialog Fragmnet
    DialogFragment(), DatePickerDialog.OnDateSetListener {                       //Implementa esta función q cada vez q se cree un dialog picker se le va a llamar


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val fecha= Calendar.getInstance()                                       //Clase para saber la fecha
        val dia= fecha.get(Calendar.DAY_OF_MONTH)
        val mes = fecha.get(Calendar.MONTH)
        val anho= fecha.get(Calendar.YEAR)

        val ponFecha = DatePickerDialog(activity as Context, this,anho,mes,dia)

        return ponFecha

    }

    override fun onDateSet(p0: DatePicker?, anho: Int, mes: Int, dia: Int) {                                //+1 Porque enero empieza en 0
        listener(dia, (mes+1), anho)
    }


}

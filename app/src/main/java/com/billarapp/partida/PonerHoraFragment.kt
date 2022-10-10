package com.billarapp.partida

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class PonerHoraFragment(val listener: (horas:Int, mes:Int) -> Unit): DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    override fun onTimeSet(p0: TimePicker?, horas: Int, minutos: Int) {             //Devuelve la hora y los minutos
        listener(horas,minutos)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {              //Funcion q devuelve el dialogo del time picker

        val hora = Calendar.getInstance()
        val horas = hora.get(Calendar.HOUR_OF_DAY)
        val minutos = hora.get(Calendar.MINUTE)

        val ponHora = TimePickerDialog(activity as Context,this,horas,minutos,true)  // Activity como contexto, el listener como ya está implementado this

        return ponHora
    }
}
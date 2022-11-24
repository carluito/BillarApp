package com.billarapp.partida.verPartidas

import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp
import java.util.*


data class Partida(var Jugador: String?=null,
                   var Provincia: String?=null,
                   var Localidad: String?=null,
                   var Local: String?=null,
                   var Fecha: String?=null,
                   var Hora: String?=null,
                   var Nivel: String?=null,
                   var NombreOponente: String?=null,
                   var EmailOponente: String?=null,
                   var Email: String?=null,
                   @ServerTimestamp
                   var FechaHora: Date?=null,                                           //Para poner la fecha y la hora en formato timestamp y así poder compararla
                   var Jugada: Boolean=false)

package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners

import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park

interface OnParksChanged {

    fun onParksChanged(parks: List<Park>)

    fun onRequestSuccess()

    fun onRequestFailure()

    fun onDistanceChanged()
}
package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners

import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle

interface OnVehiclesChanged {

    fun onVehiclesListChanged(vehicles: List<Vehicle>)

    fun onVehicleAdded()

    fun onVehicleDeleted()

    fun onVehicleUpdated(vehicle: Vehicle)
}
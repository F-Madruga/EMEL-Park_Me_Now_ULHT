package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels

import android.app.Application
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle

class VehicleFormViewModel(application: Application) : VehiclesViewModel(application) {

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        this.vehicles = vehicles
        notifyOnVehiclesChanged()
    }

    override fun onVehicleAdded() {
        notifyOnVehicleAdded()
    }

    override fun onVehicleDeleted() {
        notifyOnVehiclesDeleted()
    }

    override fun onVehicleUpdated(vehicle: Vehicle) {
        notifyOnVehiclesUpdated(vehicle)
    }
}
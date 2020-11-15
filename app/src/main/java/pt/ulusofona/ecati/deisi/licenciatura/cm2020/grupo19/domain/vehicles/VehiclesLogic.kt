package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.domain.vehicles

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories.VehicleRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged

class VehiclesLogic(private val repository: VehicleRepository) {

    fun getVehicles(listener: OnVehiclesChanged) {
        CoroutineScope(Dispatchers.IO).launch {
            listener.onVehiclesListChanged(repository.getVehicles())
        }
    }

    fun getVehicleById(listener: OnVehiclesChanged, vehicleId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            listener.onVehiclesListChanged(
                repository.getVehicles().filter { vehicle -> vehicle.uuid == vehicleId })
        }
    }

    fun addVehicle(listener: OnVehiclesChanged, vehicle: Vehicle) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertVehicle(vehicle)
            listener.onVehicleAdded()
        }
    }

    fun updateVehicle(listener: OnVehiclesChanged, vehicle: Vehicle) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateVehicles(vehicle)
            listener.onVehicleUpdated(vehicle)
        }
    }

    fun deleteVehicle(listener: OnVehiclesChanged, vehicleId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getVehicles().filter { vehicle -> vehicle.uuid == vehicleId }[0]?.let {
                repository.deleteVehicles(it)
            }
            listener.onVehicleDeleted()
        }
    }

    fun deleteAllVehicles(listener: OnVehiclesChanged) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
            listener.onVehiclesListChanged(repository.getVehicles())
        }
    }
}
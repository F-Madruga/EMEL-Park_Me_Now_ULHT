package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.ParksDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories.VehicleRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.domain.vehicles.VehiclesLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged

abstract class VehiclesViewModel(application: Application) : AndroidViewModel(application),
    OnVehiclesChanged {

    var vehicleId: String? = null
    protected var listener: OnVehiclesChanged? = null
    protected var vehicles: List<Vehicle> = listOf()

    protected val repository: VehicleRepository = VehicleRepository(
        ParksDatabase.getInstance(application).VehicleDao()
    )

    protected val vehiclesLogic: VehiclesLogic = VehiclesLogic(repository)

    protected fun notifyOnVehiclesChanged() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onVehiclesListChanged(vehicles)
        }
    }

    protected fun notifyOnVehicleAdded() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onVehicleAdded()
        }
    }

    protected fun notifyOnVehiclesDeleted() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onVehicleDeleted()
        }
    }

    protected fun notifyOnVehiclesUpdated(vehicle: Vehicle) {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onVehicleUpdated(vehicle)
        }
    }

    fun unregisterListener() {
        listener = null
    }

    fun registerListener(listener: OnVehiclesChanged?, vehicleId: String? = null) {
        this.listener = listener
        vehicleId?.let { this.vehicleId = it }
        //notifyOnVehiclesChanged()
    }

    fun getAll() {
        vehiclesLogic.getVehicles(this)
    }

    fun getVehicleById() {
        this.vehicleId?.let { vehiclesLogic.getVehicleById(this, it) }
    }

    fun addVehicle(vehicle: Vehicle) {
        vehiclesLogic.addVehicle(this, vehicle)
    }

    fun editVehicle(vehicle: Vehicle) {
        vehiclesLogic.updateVehicle(this, vehicle)
    }

    fun onClickDelete() {
        this.vehicleId?.let { vehiclesLogic.deleteVehicle(this, it) }
    }

}
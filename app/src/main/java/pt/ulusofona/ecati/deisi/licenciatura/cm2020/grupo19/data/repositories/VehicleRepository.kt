package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories

import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao.VehicleDao

class VehicleRepository(private val local: VehicleDao) {

    suspend fun getVehicles(): List<Vehicle> {
        return local.getVehicles()
    }

    suspend fun insertVehicle(vehicle: Vehicle) {
        local.insert(vehicle)
    }

    suspend fun updateVehicles(vehicle: Vehicle) {
        local.update(vehicle)
    }

    suspend fun deleteVehicles(vehicle: Vehicle) {
        local.delete(vehicle)
    }

    suspend fun deleteAll() {
        local.deleteAll()
    }
}
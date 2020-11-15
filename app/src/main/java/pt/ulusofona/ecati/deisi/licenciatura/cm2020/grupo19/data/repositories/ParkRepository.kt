package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories

import android.location.Location
import android.util.Log
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Zone
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao.ParkDao
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.services.ParkService
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import retrofit2.Retrofit
import java.lang.Exception

class ParkRepository(private val local: ParkDao, private val remote: Retrofit) {

    private val TAG = ParkRepository::class.java.simpleName

    suspend fun getParkingLots(): List<Park> {
        return local.getParkingLots()
    }

    suspend fun updateDistance(listener: OnParksChanged, location: Location) {
        local.getParkingLots().forEach {
            it.updateDistance(location)
            local.updatePark(it)
        }
        listener.onDistanceChanged()
    }

    suspend fun updateLocalParkingLot(listener: OnParksChanged, apiKey: String) {
        val service = remote.create(ParkService::class.java)
        try {
            val response = service.getParkingLots(apiKey)
            if (response.isSuccessful) {
                val parkingLots = response.body()
                    ?.map { getParkingLotsResponse -> getParkingLotsResponse.toParkingLot() }
                    ?: listOf()
                local.deleteAll()
                parkingLots.forEach {
                    it.zone = getParkZone(apiKey, it.latitude, it.longitude)
                    local.insert(it)
                }
                listener.onRequestSuccess()
            } else {
                // Response error
                listener.onRequestFailure()
            }
        } catch (e : Exception) {
            // No access to Internet
            listener.onRequestFailure()
        }
    }

    suspend fun getParkZone(apiKey: String, latitude: Double, longitude: Double): Zone? {
        try {
            val service = remote.create(ParkService::class.java)
            val response = service.getParkingZone(apiKey, latitude, longitude)
            if (response.isSuccessful) {
                return response.body()?.toZone()
            }
            else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }
}
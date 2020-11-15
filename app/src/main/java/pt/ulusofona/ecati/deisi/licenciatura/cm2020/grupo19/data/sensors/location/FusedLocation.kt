package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

class FusedLocation private constructor(context: Context) : LocationCallback() {

    private val TIME_BETWEEN_UPDATES = 30 * 1000L

    private var locationRequest: LocationRequest? = null

    private val client = FusedLocationProviderClient(context)

    init {
        locationRequest = LocationRequest()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = TIME_BETWEEN_UPDATES

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
            .build()

        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)
    }

    private fun startLocationUpdates() {
        client.requestLocationUpdates(locationRequest, this, Looper.myLooper())
    }

    override fun onLocationResult(locationResult: LocationResult?) {
        locationResult?.let { notifyListeners(it) }
        super.onLocationResult(locationResult)
    }

    companion object {

        private var listeners: MutableList<OnLocationChangedListener> = mutableListOf()
        private var instance: FusedLocation? = null

        fun registerListener(listener: OnLocationChangedListener) {
            synchronized(this) {
                this.listeners.add(listener)
            }
        }

        fun unregisterListener(listener: OnLocationChangedListener) {
            synchronized(this) {
                this.listeners.remove(listener)
            }
        }

        fun notifyListeners(locationResult: LocationResult) {
            listeners.forEach { it.onLocationChangedListener(locationResult) }
        }

        fun start(context: Context) {
            instance = if (instance == null) FusedLocation(context) else instance
            instance?.startLocationUpdates()
        }

    }
}
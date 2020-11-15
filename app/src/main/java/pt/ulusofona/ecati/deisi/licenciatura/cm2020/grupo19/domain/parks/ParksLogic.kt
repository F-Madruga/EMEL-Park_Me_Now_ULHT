package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.domain.parks


import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories.ParkRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.Accelerometer
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.OnAccelerometerChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery.OnBatteryCurrentListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged

class ParksLogic(private val repository: ParkRepository) : OnLocationChangedListener,
    OnParksChanged, OnAccelerometerChangedListener, OnBatteryCurrentListener {

    private val TAG = ParksLogic::class.java.simpleName
    private var listener: OnParksChanged? = null
    private var locationListener: OnLocationChangedListener? = null
    private var batteryListener: OnBatteryCurrentListener? = null
    private var accelerometerListener: OnAccelerometerChangedListener? = null

    companion object {
        private var locationResult: LocationResult? = null
    }


    fun registerListener(listener: OnParksChanged?, locationListener: OnLocationChangedListener?, accelerometerListener: OnAccelerometerChangedListener?, batteryListener: OnBatteryCurrentListener?) {
        this.listener = listener
        this.locationListener = locationListener
        this.accelerometerListener = accelerometerListener
        this.batteryListener = batteryListener
        FusedLocation.registerListener(this)
        Accelerometer.registerListener(this)
        Battery.registerListener(this)
    }

    fun unregisterListener() {
        this.listener = null
        this.locationListener = null
        this.accelerometerListener = null
        FusedLocation.unregisterListener(this)
        Accelerometer.unregisterListener(this)
        Battery.unregisterListener(this)
    }

    override fun onParksChanged(parks: List<Park>) {
        listener?.onParksChanged(parks)
    }

    override fun onRequestSuccess() {
        CoroutineScope(Dispatchers.IO).launch {
            locationResult?.lastLocation?.let { repository.updateDistance(this@ParksLogic, it) }
            listener?.onRequestSuccess()
        }
    }

    override fun onRequestFailure() {
        CoroutineScope(Dispatchers.IO).launch {
            locationResult?.lastLocation?.let { repository.updateDistance(this@ParksLogic, it) }
            listener?.onRequestFailure()
        }
    }

    override fun onDistanceChanged() {
        listener?.onDistanceChanged()
    }

    override fun onLocationChangedListener(locationResult: LocationResult) {
        CoroutineScope(Dispatchers.IO).launch {
            ParksLogic.locationResult = locationResult
            repository.updateDistance(this@ParksLogic, locationResult.lastLocation)
            //locationListener?.onLocationChangedListener(locationResult)
        }
    }

    override fun onCurrentChanged(current: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            batteryListener?.onCurrentChanged(current)
        }
    }

    fun getCurrentLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            ParksLogic.locationResult?.let { locationListener?.onLocationChangedListener(it) }
        }
    }

    fun getParkingLots(sharedPreferences: SharedPreferences? = null, comparator: Comparator<Park>? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            var parks = repository.getParkingLots()
            // Filtering
            sharedPreferences?.let {
                parks = parks
                    .filter { park -> it.getString("type", "All") == "All"
                                || park.type == it.getString("type", "All")
                    }.filter { park -> it.getString("availability", "All") == "All"
                                || park.availability == it.getString("availability", "All")
                    }.filter { park -> it.getString("distance", "100") == "100"
                                || park.distance.toInt() <= (it.getString("distance", "10000000")
                            .toInt() * 50) + 50
                    }
            }
            // Sorting
            comparator?.let {
                parks = parks.sortedWith(it)
            }
            listener?.onParksChanged(parks)
        }
    }

    fun parkMeNow() {
        CoroutineScope(Dispatchers.IO).launch {
            listener?.onParksChanged(
                repository.getParkingLots()
                .filter { park -> park.availability != "Full" }
                .sortedWith(compareBy<Park>{it.distance})
            )
        }
    }

    fun loadParks(apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateLocalParkingLot(this@ParksLogic, apiKey)
        }
    }

    fun getParkingLotById(parkId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            listener?.onParksChanged(
                repository.getParkingLots().filter { park -> park.parkId == parkId }
            )
        }
    }

    override fun onAccelerometerChangedListener(acceleration: Float) {
        if (acceleration > 16) {
            accelerometerListener?.onAccelerometerChangedListener(acceleration)
        }
    }
}
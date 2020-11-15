package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.ParksDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.RetrofitBuilder
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.repositories.ParkRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.OnAccelerometerChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery.OnBatteryCurrentListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.domain.parks.ParksLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged

const val ENDPOINT = "https://emel.city-platform.com/opendata/"
const val API_KEY = "93600bb4e7fee17750ae478c22182dda"

abstract class ParksViewModel(application: Application) : AndroidViewModel(application),
    OnParksChanged, OnLocationChangedListener, OnAccelerometerChangedListener, OnBatteryCurrentListener {

    protected var listener: OnParksChanged? = null
    protected var locationListener: OnLocationChangedListener? = null
    protected var accelerometerListener: OnAccelerometerChangedListener? = null
    protected var batteryListener: OnBatteryCurrentListener? = null
    protected var parkId: String? = null
    var parks: List<Park> = listOf()
    private val TAG = ParksViewModel::class.java.simpleName

    protected val repository: ParkRepository = ParkRepository(
        ParksDatabase.getInstance(application).ParkDao(),
        RetrofitBuilder.getInstance(ENDPOINT)
    )

    protected val parksLogic: ParksLogic = ParksLogic(repository)

    protected fun notifyOnParksChanged() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onParksChanged(parks)
        }
    }

    protected fun notifyOnDistanceChanged() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onDistanceChanged()
        }
    }

    protected fun notifyOnRequestSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onRequestSuccess()
        }
    }

    protected fun notifyOnRequestFailure() {
        Log.i(TAG, "Local Data")
        CoroutineScope(Dispatchers.Main).launch {
            listener?.onRequestFailure()
        }
    }

    protected fun notifyOnLocationChanged(locationResult: LocationResult) {
        CoroutineScope(Dispatchers.Main).launch {
            locationListener?.onLocationChangedListener(locationResult)
        }
    }

    protected fun notifyAccelerometerChanged(acceleration: Float) {
        CoroutineScope(Dispatchers.Main).launch {
            accelerometerListener?.onAccelerometerChangedListener(acceleration)
        }
    }

    protected fun notifyBatteryChanged(current: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            batteryListener?.onCurrentChanged(current)
        }
    }

    fun registerListener(listener: OnParksChanged?, locationListener: OnLocationChangedListener? = null, accelerometerListener: OnAccelerometerChangedListener? = null, batteryListener: OnBatteryCurrentListener? = null, parkId: String? = null) {
        this.listener = listener
        this.locationListener = locationListener
        this.accelerometerListener = accelerometerListener
        this.batteryListener = batteryListener
        this.parksLogic.registerListener(this, this, this, this)
        parkId?.let { this.parkId = it }
    }

    fun unregisterListener() {
        this.listener = null
        this.locationListener = null
        this.accelerometerListener = null
        this.batteryListener = null
        this.parksLogic.unregisterListener()
    }

    fun loadData() {
        parksLogic.loadParks(API_KEY)
    }

    fun getAll(sharedPreferences: SharedPreferences? = null, comparator: Comparator<Park>? = null) {
        this.parksLogic.getParkingLots(sharedPreferences, comparator)
    }

    fun parkMeNow() {
        this.parksLogic.parkMeNow()
    }

    fun getParkById() {
        this.parkId?.let { this.parksLogic.getParkingLotById(it) }
    }

    fun getCurrentLocation() {
        this.parksLogic.getCurrentLocation()
    }

}
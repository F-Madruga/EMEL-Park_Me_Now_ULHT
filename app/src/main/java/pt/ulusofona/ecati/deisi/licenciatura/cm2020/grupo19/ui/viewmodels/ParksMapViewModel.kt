package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged

class ParksMapViewModel(application: Application) : ParksViewModel(application) {

    override fun onParksChanged(parks: List<Park>) {
        this.parks = parks
        notifyOnParksChanged()
    }

    override fun onRequestSuccess() {
        notifyOnRequestSuccess()
    }

    override fun onRequestFailure() {
        notifyOnRequestFailure()
    }

    override fun onDistanceChanged() {
        notifyOnDistanceChanged()
    }

    override fun onLocationChangedListener(locationResult: LocationResult) {
        notifyOnLocationChanged(locationResult)
    }

    override fun onAccelerometerChangedListener(acceleration: Float) {
        notifyAccelerometerChanged(acceleration)
    }

    override fun onCurrentChanged(current: Double) {
        notifyBatteryChanged(current)
    }

}
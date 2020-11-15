package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels

import android.app.Application
import android.location.Location
import com.google.android.gms.location.LocationResult
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park

class ParkDetailsViewModel(application: Application) : ParksViewModel(application) {

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
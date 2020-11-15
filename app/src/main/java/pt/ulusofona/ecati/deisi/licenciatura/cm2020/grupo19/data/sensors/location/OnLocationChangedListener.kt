package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location

import com.google.android.gms.location.LocationResult

interface OnLocationChangedListener {

    fun onLocationChangedListener(locationResult: LocationResult)

}
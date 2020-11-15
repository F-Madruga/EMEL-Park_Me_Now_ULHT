package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.Accelerometer
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.PREFERENCES_FILE

class ParkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(
                "applicationTheme",
                AppCompatDelegate.MODE_NIGHT_NO
            )
        )
        FusedLocation.start(this)
        Accelerometer.start(this)
        Battery.start(this)
    }

}
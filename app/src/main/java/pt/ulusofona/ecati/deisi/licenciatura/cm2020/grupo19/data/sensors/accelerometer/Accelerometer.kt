package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.OnLocationChangedListener
import java.util.*

class Accelerometer private constructor(context: Context) : SensorEventListener {

    private val TAG = Accelerometer::class.java.simpleName

    private var sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    companion object {

        private var listeners: MutableList<OnAccelerometerChangedListener> = mutableListOf()
        private var instance: Accelerometer? = null
        private lateinit var context: Context

        fun registerListener(listener: OnAccelerometerChangedListener) {
            synchronized(this) {
                this.listeners.add(listener)
            }
        }

        fun unregisterListener(listener: OnAccelerometerChangedListener) {
            synchronized(this) {
                this.listeners.remove(listener)
            }
        }

        fun notifyListeners(acceleration: Float) {
            Accelerometer.listeners.forEach { it.onAccelerometerChangedListener(acceleration) }
        }

        fun start(context: Context) {
            this.context = context
            instance = if (instance == null) Accelerometer(context) else instance
            instance?.start()
        }
    }

    private fun start() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event?.values?.get(0)
        val y = event?.values?.get(1)
        val z = event?.values?.get(2)
        lastAcceleration = currentAcceleration
        if (x != null && y != null && z != null) {
            currentAcceleration = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }
        val delta: Float = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta
        notifyListeners(acceleration)
    }


}
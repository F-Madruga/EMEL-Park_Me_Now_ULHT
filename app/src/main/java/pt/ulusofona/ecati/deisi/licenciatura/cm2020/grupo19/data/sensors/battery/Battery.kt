package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery

import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.util.Log

class Battery private constructor(private val context: Context): Runnable {

    private val TAG = Battery::class.java.simpleName

    private val TIME_BETWEEN_UPDATES = 2000L

    companion object {

        private var listeners: MutableList<OnBatteryCurrentListener> = mutableListOf()
        private var instance: Battery? = null
        private val handler = Handler()

        fun registerListener(listener: OnBatteryCurrentListener) {
            synchronized(this) {
                this.listeners.add(listener)
            }
        }

        fun unregisterListener(listener: OnBatteryCurrentListener) {
            synchronized(this) {
                this.listeners.remove(listener)
            }
        }

        fun notifyListeners(current: Double) {
            Battery.listeners.forEach { it.onCurrentChanged(current) }
        }

        fun start(context: Context) {
            instance = if (instance == null) Battery(context) else instance
            instance?.start()
        }

    }

    private  fun start() {
        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }

    private fun getBatteryCurrentNow(): Double {
        val manager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val value = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        return if (value != 0 && value != Int.MIN_VALUE) value.toDouble() / 1000000 else 0.0
    }

    override fun run() {
        val current = getBatteryCurrentNow()
        Log.i(TAG, current.toString())
        notifyListeners(current)
        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }
}
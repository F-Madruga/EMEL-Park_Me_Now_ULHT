package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao.ParkDao
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao.VehicleDao


@Database(entities = arrayOf(Park::class, Vehicle::class), version = 1)
abstract class ParksDatabase : RoomDatabase() {

    abstract fun ParkDao(): ParkDao
    abstract fun VehicleDao(): VehicleDao

    companion object {

        private var instance: ParksDatabase? = null

        fun getInstance(applicationContext: Context): ParksDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        applicationContext,
                        ParksDatabase::class.java,
                        "parks_db"
                    ).build()
                }
                return instance as ParksDatabase
            }
        }
    }
}
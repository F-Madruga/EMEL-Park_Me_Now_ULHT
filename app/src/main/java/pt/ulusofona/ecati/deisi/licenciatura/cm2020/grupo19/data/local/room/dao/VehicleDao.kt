package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle

@Dao
interface VehicleDao {

    @Insert()
    fun insert(vehicle: Vehicle)

    @Query("SELECT * FROM vehicle")
    suspend fun getVehicles(): List<Vehicle>

    @Query("DELETE FROM vehicle")
    suspend fun deleteAll()

    @Delete()
    fun delete(vehicle: Vehicle)

    @Update(onConflict = REPLACE)
    fun update(vehicle: Vehicle)
}
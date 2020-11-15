package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park

@Dao
interface ParkDao {

    @Insert
    suspend fun insert(park: Park)

    @Update
    suspend fun updatePark(park: Park)

    @Query("SELECT * FROM park")
    suspend fun getParkingLots(): List<Park>

    @Query("DELETE FROM park")
    suspend fun deleteAll()
}
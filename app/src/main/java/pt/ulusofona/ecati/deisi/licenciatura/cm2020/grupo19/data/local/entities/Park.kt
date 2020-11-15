package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities

import android.location.Location
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity
data class Park(
    @PrimaryKey
    val parkId: String,
    val name: String,
    val active: Int,
    val entityId: Int,
    val maxCapacity: Int,
    val occupation: Int,
    val _occupationDate: String,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    @Embedded var zone: Zone? = null
) {

    var distance: Float = 0f

    val occupationDate get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this._occupationDate)

    val availability
        get(): String {
            if (this.maxCapacity == 0 || (this.occupation / this.maxCapacity) == 1) {
                return "Full"
            } else if ((this.occupation.toDouble() / this.maxCapacity.toDouble()) > 0.9) {
                return "Potentially Full"
            } else {
                return "Empty"
            }
        }

    fun updateDistance(location: Location) {
        val parkLocation = Location(name)
        parkLocation.latitude = latitude
        parkLocation.longitude = longitude
        distance = location.distanceTo(parkLocation)
    }

    override fun toString(): String {
        return "${this.parkId} | ${this.name} | ${this.active} | ${this.entityId} | ${this.maxCapacity} | ${this.occupation} | ${this._occupationDate} | (${this.latitude}, ${this.longitude}) | ${this.type}"
    }
}
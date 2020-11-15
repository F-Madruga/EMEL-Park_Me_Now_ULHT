package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Zone(
    @PrimaryKey
    val zoneId: Int,
    val product: String,
    val tariffCod: String,
    val tariff: String,
    val scheduleCod: String,
    val schedule: String,
    val parkTypeId: String,
    val parkType: String,
    val observations: String?
) {
    override fun toString(): String {
        return "${zoneId.toString()} | $product | $tariff | $schedule"
    }
}
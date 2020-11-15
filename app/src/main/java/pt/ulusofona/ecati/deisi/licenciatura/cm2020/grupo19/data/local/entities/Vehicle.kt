package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Vehicle(
    val licensePlate: String,
    val brand: String,
    val model: String,
    val _registrationDate: String,
    @Embedded var park: Park? = null
) {

    val registrationDate get() = SimpleDateFormat("dd/MM/yyyy").parse(_registrationDate)

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()

    override fun toString(): String {
        return "$licensePlate | $brand | $model | $_registrationDate"
    }
}
package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.responses

import com.google.gson.annotations.SerializedName
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import java.text.SimpleDateFormat

data class GetParkingLotsResponse(
    @SerializedName("id_parque") val parkId: String,
    @SerializedName("nome") val name: String,
    @SerializedName("activo") val active: Int,
    @SerializedName("id_entidade") val entityId: Int,
    @SerializedName("capacidade_max") val maxCapacity: Int,
    @SerializedName("ocupacao") val occupation: Int,
    @SerializedName("data_ocupacao") val _occupationDate: String,
    @SerializedName("latitude") val _latitude: String,
    @SerializedName("longitude") val _longitude: String,
    @SerializedName("tipo") val type: String
) {
    val occupationDate get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this._occupationDate)

    val latitude get() = this._latitude.toDouble()

    val longitude get() = this._longitude.toDouble()
    private val TAG = GetParkingLotsResponse::class.java.simpleName

    fun toParkingLot(): Park {
        return Park(
            this.parkId,
            this.name,
            this.active,
            this.entityId,
            this.maxCapacity,
            this.occupation,
            this._occupationDate,
            this.latitude,
            this.longitude,
            this.type
        )
    }

    override fun toString(): String {
        return "${this.parkId} | ${this.name} | ${this.active} | ${this.entityId} | ${this.maxCapacity} | ${this.occupation} | ${this._occupationDate} | (${this._latitude}, ${this._longitude}) | ${this.type}"
    }
}
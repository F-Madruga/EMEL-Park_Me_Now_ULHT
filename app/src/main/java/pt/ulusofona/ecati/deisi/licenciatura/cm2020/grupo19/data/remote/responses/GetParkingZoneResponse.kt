package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.responses

import com.google.gson.annotations.SerializedName
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Zone

class GetParkingZoneResponse(
    @SerializedName("id") val zoneId: Int,
    @SerializedName("produto") val product: String,
    @SerializedName("cod_tarifa") val tariffCod: String,
    @SerializedName("tarifa") val tariff: String,
    @SerializedName("cod_horario") val scheduleCod: String,
    @SerializedName("horario") val schedule: String,
    @SerializedName("id_tipo_estacionamento") val parkTypeId: String,
    @SerializedName("tipo_estacionamento") val parkType: String,
    @SerializedName("observacoes") val observations: String?
) {

    fun toZone(): Zone {
        return Zone(zoneId, product, tariffCod, tariff, scheduleCod, schedule, parkTypeId, parkType, observations)
    }

    override fun toString(): String {
        return "${zoneId.toString()} | $product | $tariff | $schedule"
    }
}
package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.services

import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.responses.GetParkingLotsResponse
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote.responses.GetParkingZoneResponse
import retrofit2.Response
import retrofit2.http.*

interface ParkService {

    @GET("parking/lots")
    suspend fun getParkingLots(@Header("api_key") apiKey: String): Response<List<GetParkingLotsResponse>>

    @GET("parking/zone")
    suspend fun getParkingZone(@Header("api_key") apiKey: String, @Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Response<GetParkingZoneResponse>
}
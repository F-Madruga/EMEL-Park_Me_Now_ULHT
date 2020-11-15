package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    companion object {

        fun getInstance(path: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
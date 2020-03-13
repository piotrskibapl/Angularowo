package pl.piotrskiba.angularowo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MojangAPIClient {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.mojang.com/"

    @JvmStatic
    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }
}
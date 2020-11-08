package pl.piotrskiba.angularowo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MojangAPIClient {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.mojang.com/"

    @JvmStatic
    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
                val client = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build()
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
            }
            return retrofit!!
        }
}
package pl.piotrskiba.angularowo.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerAPIClient {
    const val API_KEY = "ySrfs3nAqaw28D8W1KJPz3HT8IAydXnY"
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://piotrskiba.pl/angularowo/api/v1/"

    @JvmStatic
    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                val unauthorizedInterceptor = UnauthorizedInterceptor()
                val client = OkHttpClient.Builder()
                        .addInterceptor(unauthorizedInterceptor)
                        .build()
                client.dispatcher.maxRequests = 1
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
            }
            return retrofit!!
        }
}
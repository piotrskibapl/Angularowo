package pl.piotrskiba.angularowo.data.network.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.piotrskiba.angularowo.data.network.interceptors.UnauthorizedInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .addInterceptor(UnauthorizedInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl("https://piotrskiba.pl/angularowo/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}
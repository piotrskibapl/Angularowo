package pl.piotrskiba.angularowo.data.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.network.NetworkRepositoryImpl
import pl.piotrskiba.angularowo.data.network.interceptors.UnauthorizedInterceptor
import pl.piotrskiba.angularowo.domain.network.repository.NetworkRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            val level = if (BuildConfig.DEBUG) Level.BASIC else Level.NONE
            setLevel(level)
        }

    @Singleton
    @Provides
    fun provideUnauthorizedInterceptor(): UnauthorizedInterceptor =
        UnauthorizedInterceptor()

    @Provides
    fun provideUnauthorizedRepository(unauthorizedInterceptor: UnauthorizedInterceptor): NetworkRepository =
        NetworkRepositoryImpl(unauthorizedInterceptor)

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        unauthorizedInterceptor: UnauthorizedInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://piotrskiba.pl/angularowo/api/v1.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
}

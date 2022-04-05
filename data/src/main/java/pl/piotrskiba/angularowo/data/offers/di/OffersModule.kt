package pl.piotrskiba.angularowo.data.offers.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.offers.OffersApiService
import pl.piotrskiba.angularowo.data.offers.repository.OffersRepositoryImpl
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class OffersModule {

    @Provides
    fun provideOffersApiService(retrofit: Retrofit): OffersApiService =
        retrofit.create(OffersApiService::class.java)

    @Provides
    fun provideOffersRepository(offersApiService: OffersApiService): OffersRepository =
        OffersRepositoryImpl(offersApiService)
}

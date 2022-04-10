package pl.piotrskiba.angularowo.data.player.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.repository.PlayerRepositoryImpl
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class PlayerModule {

    @Provides
    fun providePlayerApiService(retrofit: Retrofit): PlayerApiService =
        retrofit.create(PlayerApiService::class.java)

    @Provides
    @Singleton // TODO: use proper scope
    fun providePlayerRepository(playerApiService: PlayerApiService): PlayerRepository =
        PlayerRepositoryImpl(playerApiService)
}

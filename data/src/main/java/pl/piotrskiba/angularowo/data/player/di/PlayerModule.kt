package pl.piotrskiba.angularowo.data.player.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.mapper.DetailedPlayerMapper
import pl.piotrskiba.angularowo.data.player.repository.PlayerRepositoryImpl
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUsernameUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class PlayerModule {

    @Provides
    fun providePlayerApiService(retrofit: Retrofit) =
        retrofit.create(PlayerApiService::class.java)

    @Provides
    fun providePlayerRepository(playerApiService: PlayerApiService): PlayerRepository =
        PlayerRepositoryImpl(playerApiService, DetailedPlayerMapper())

    @Provides
    fun provideGetPlayerDetailsFromUsernameDeviceUseCase(playerRepository: PlayerRepository) =
        GetPlayerDetailsFromUsernameUseCase(playerRepository)

    @Provides
    fun provideGetPlayerDetailsFromUuidDeviceUseCase(playerRepository: PlayerRepository) =
        GetPlayerDetailsFromUuidUseCase(playerRepository)
}

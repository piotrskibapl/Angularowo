package pl.piotrskiba.angularowo.data.player.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.repository.PlayerRepositoryImpl
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.player.usecase.ObserveOnlinePlayerListWithFavoriteInformationUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUsernameUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
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

    @Provides
    fun provideGetPlayerDetailsFromUsernameDeviceUseCase(
        playerRepository: PlayerRepository,
        rankRepository: RankRepository
    ) = GetPlayerDetailsFromUsernameUseCase(playerRepository, rankRepository)

    @Provides
    fun provideGetPlayerDetailsFromUuidDeviceUseCase(
        playerRepository: PlayerRepository,
        rankRepository: RankRepository
    ) = GetPlayerDetailsFromUuidUseCase(playerRepository, rankRepository)

    @Provides
    fun provideGetOnlinePlayerListUseCase(
        playerRepository: PlayerRepository,
        rankRepository: RankRepository,
        friendRepository: FriendRepository
    ) = ObserveOnlinePlayerListWithFavoriteInformationUseCase(playerRepository, rankRepository, friendRepository)
}

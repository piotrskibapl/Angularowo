package pl.piotrskiba.angularowo.data.player.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.model.PlayerData
import pl.piotrskiba.angularowo.data.player.model.toDomain
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerApi: PlayerApiService
) : PlayerRepository {

    private var onlinePlayerList: BehaviorSubject<List<PlayerData>> = BehaviorSubject.create()

    override fun getPlayerDetailsFromUsername(
        accessToken: String,
        username: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUsername(BuildConfig.API_KEY, username, accessToken)
            .map { it.toDomain() }

    override fun getPlayerDetailsFromUuid(
        accessToken: String,
        uuid: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUuid(BuildConfig.API_KEY, uuid, accessToken)
            .map { it.toDomain() }

    override fun refreshOnlinePlayerList(
        accessToken: String
    ): Completable =
        Completable.fromSingle(
            playerApi
                .getOnlinePlayerList(BuildConfig.API_KEY, accessToken)
                .doAfterSuccess { onlinePlayerList.onNext(it) }
        )

    override fun observeOnlinePlayerList(): Observable<List<Player>> =
        onlinePlayerList.map { playerList ->
            playerList.map { it.toDomain() }
        }
}

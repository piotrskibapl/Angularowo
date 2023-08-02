package pl.piotrskiba.angularowo.data.player.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.model.PlayerRemote
import pl.piotrskiba.angularowo.data.player.model.toDomain
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val playerApi: PlayerApiService,
) : PlayerRepository {

    private var onlinePlayerList: BehaviorSubject<List<PlayerRemote>> = BehaviorSubject.create()

    override fun getPlayerDetailsFromUsername(
        accessToken: String,
        username: String,
    ): Single<DetailedPlayerModel> =
        playerApi
            .getPlayerInfoFromUsername(username, accessToken)
            .map { it.toDomain() }

    override fun getPlayerDetailsFromUuid(
        accessToken: String,
        uuid: String,
    ): Single<DetailedPlayerModel> =
        playerApi
            .getPlayerInfoFromUuid(uuid, accessToken)
            .map { it.toDomain() }

    override fun refreshOnlinePlayerList(
        accessToken: String,
    ): Completable =
        Completable.fromSingle(
            playerApi
                .getOnlinePlayerList(accessToken)
                .doAfterSuccess { onlinePlayerList.onNext(it) },
        )

    override fun observeOnlinePlayerList(): Observable<List<PlayerModel>> =
        onlinePlayerList.map { playerList ->
            playerList.map { it.toDomain() }
        }
}

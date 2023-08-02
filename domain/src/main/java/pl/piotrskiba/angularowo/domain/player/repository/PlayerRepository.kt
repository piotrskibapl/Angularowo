package pl.piotrskiba.angularowo.domain.player.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel

interface PlayerRepository {

    fun getPlayerDetailsFromUsername(
        username: String,
    ): Single<DetailedPlayerModel>

    fun getPlayerDetailsFromUuid(
        uuid: String,
    ): Single<DetailedPlayerModel>

    fun observeOnlinePlayerList(): Observable<List<PlayerModel>>

    fun refreshOnlinePlayerList(): Completable
}

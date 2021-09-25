package pl.piotrskiba.angularowo.domain.player.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.model.Player

interface PlayerRepository {

    fun getPlayerDetailsFromUsername(
        apiKey: String,
        accessToken: String,
        username: String
    ): Single<DetailedPlayer>

    fun getPlayerDetailsFromUuid(
        apiKey: String,
        accessToken: String,
        uuid: String
    ): Single<DetailedPlayer>

    fun observeOnlinePlayerList(): Observable<List<Player>>

    fun refreshOnlinePlayerList(
        apiKey: String,
        accessToken: String
    ): Completable
}

package pl.piotrskiba.angularowo.domain.player.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer

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
}

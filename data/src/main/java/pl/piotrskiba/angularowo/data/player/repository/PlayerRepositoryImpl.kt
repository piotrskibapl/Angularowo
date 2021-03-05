package pl.piotrskiba.angularowo.data.player.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.model.toDomain
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerApi: PlayerApiService
) : PlayerRepository {

    override fun getPlayerDetailsFromUsername(
        apiKey: String,
        accessToken: String,
        username: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUsername(apiKey, username, accessToken)
            .map { it.toDomain() }

    override fun getPlayerDetailsFromUuid(
        apiKey: String,
        accessToken: String,
        uuid: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUuid(apiKey, uuid, accessToken)
            .map { it.toDomain() }

    override fun getOnlinePlayerList(
        apiKey: String,
        accessToken: String
    ): Single<List<Player>> =
        playerApi
            .getOnlinePlayerList(apiKey, accessToken)
            .map { playerList ->
                playerList.map { it.toDomain() }
            }
}

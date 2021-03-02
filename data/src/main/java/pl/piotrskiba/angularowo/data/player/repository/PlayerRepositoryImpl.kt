package pl.piotrskiba.angularowo.data.player.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.mapper.DetailedPlayerMapper
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerApi: PlayerApiService,
    private val detailedPlayerMapper: DetailedPlayerMapper
) : PlayerRepository {

    override fun getPlayerDetailsFromUsername(
        apiKey: String,
        accessToken: String,
        username: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUsername(apiKey, username, accessToken)
            .map { detailedPlayerMapper.toDetailedPlayer(it) }

    override fun getPlayerDetailsFromUuid(
        apiKey: String,
        accessToken: String,
        uuid: String
    ): Single<DetailedPlayer> =
        playerApi
            .getPlayerInfoFromUuid(apiKey, uuid, accessToken)
            .map { detailedPlayerMapper.toDetailedPlayer(it) }
}

package pl.piotrskiba.angularowo.domain.mainscreen.usecase

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository
import javax.inject.Inject

class GetMainScreenDataAndSavePlayerUseCase @Inject constructor(
    private val serverRepository: ServerRepository,
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
    private val punishmentRepository: PunishmentRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(): Single<MainScreenDataModel> =
        Maybe.zip(
            preferencesRepository.accessToken(),
            preferencesRepository.uuid(),
            preferencesRepository.username(),
            ::Triple
        )
            .toSingle()
            .flatMap { (accessToken, uuid, username) ->
                getServerStatus(accessToken)
                    .zipWith(getAndSavePlayerData(accessToken, uuid), ::Pair)
                    .zipWith(getPlayerPunishments(accessToken, username)) { (serverStatus, playerData), playerPunishments ->
                        MainScreenDataModel(serverStatus, playerData, playerPunishments)
                    }
            }

    private fun getServerStatus(accessToken: String) =
        serverRepository.getServerStatus(accessToken)

    private fun getAndSavePlayerData(accessToken: String, uuid: String) =
        playerRepository.getPlayerDetailsFromUuid(accessToken, uuid)
            .flatMap { player ->
                preferencesRepository.setUsername(player.username)
                    .mergeWith(preferencesRepository.setRankName(player.rank.name))
                    .andThen(rankRepository.getAllRanks())
                    .map { ranks ->
                        val playerRank = ranks.firstOrNull { it.name == player.rank.name } ?: player.rank
                        player.copy(rank = playerRank)
                    }
            }

    private fun getPlayerPunishments(accessToken: String, username: String) =
        punishmentRepository.getPlayerPunishments(
            accessToken = accessToken,
            username = username,
            punishmentTypes = listOf(PunishmentType.MUTE, PunishmentType.WARN, PunishmentType.BAN),
            filter = PunishmentFilter.ACTIVE,
        )
}

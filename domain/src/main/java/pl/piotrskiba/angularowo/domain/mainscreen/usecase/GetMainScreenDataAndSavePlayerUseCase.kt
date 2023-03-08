package pl.piotrskiba.angularowo.domain.mainscreen.usecase

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

    fun execute(): Single<MainScreenDataModel> {
        val accessToken = preferencesRepository.accessToken!!
        return getServerStatus(accessToken)
            .zipWith(getAndSavePlayerData(accessToken), ::Pair)
            .zipWith(getPlayerPunishments(accessToken)) { (serverStatus, playerData), playerPunishments ->
                MainScreenDataModel(serverStatus, playerData, playerPunishments)
            }
    }

    private fun getServerStatus(accessToken: String) =
        serverRepository.getServerStatus(accessToken)

    private fun getAndSavePlayerData(accessToken: String) =
        playerRepository.getPlayerDetailsFromUuid(accessToken, preferencesRepository.uuid!!)
            .concatMap { detailedPlayerModel ->
                rankRepository.getAllRanks().map { rankList ->
                    detailedPlayerModel.rank =
                        rankList.firstOrNull {
                            it.name == detailedPlayerModel.rank.name
                        } ?: detailedPlayerModel.rank
                    detailedPlayerModel
                }
            }
            .map { detailedPlayerModel ->
                preferencesRepository.username = detailedPlayerModel.username
                preferencesRepository.rankName = detailedPlayerModel.rank.name
                detailedPlayerModel
            }

    private fun getPlayerPunishments(accessToken: String) =
        punishmentRepository.getPlayerPunishments(
            accessToken = accessToken,
            username = preferencesRepository.username!!,
            punishmentTypes = listOf(PunishmentType.MUTE, PunishmentType.WARN, PunishmentType.BAN),
            filter = PunishmentFilter.ACTIVE,
        )
}

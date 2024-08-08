package pl.piotrskiba.angularowo.domain.mainscreen.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.usecase.GetAppUserPlayerUseCase
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository
import javax.inject.Inject

class GetMainScreenDataAndSavePlayerUseCase @Inject constructor(
    private val serverRepository: ServerRepository,
    private val punishmentRepository: PunishmentRepository,
    private val preferencesRepository: PreferencesRepository,
    private val getAppUserPlayerUseCase: GetAppUserPlayerUseCase,
) {

    fun execute(): Single<MainScreenDataModel> =
        getAppUserPlayerUseCase.execute(ignoreCache = true)
            .flatMap { player ->
                Single.zip(
                    getServerStatus(),
                    savePlayerData(player),
                    getPlayerPunishments(player.username),
                    ::Triple,
                ).map { (serverStatus, playerData, playerPunishments) ->
                    MainScreenDataModel(serverStatus, playerData, playerPunishments)
                }
            }

    private fun getServerStatus() =
        serverRepository.getServerStatus()

    private fun savePlayerData(player: DetailedPlayerModel) =
        preferencesRepository.setUsername(player.username)
            .mergeWith(preferencesRepository.setRankName(player.rank.name))
            .toSingleDefault(player)

    private fun getPlayerPunishments(username: String) =
        punishmentRepository.getPlayerPunishments(
            username = username,
            punishmentTypes = listOf(
                PunishmentTypeModel.MUTE,
                PunishmentTypeModel.WARN,
                PunishmentTypeModel.BAN,
            ),
            filter = PunishmentFilterModel.ACTIVE,
        )
}

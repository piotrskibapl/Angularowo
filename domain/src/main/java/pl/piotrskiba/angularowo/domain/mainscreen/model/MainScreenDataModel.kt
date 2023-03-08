package pl.piotrskiba.angularowo.domain.mainscreen.model

import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

data class MainScreenDataModel(
    val serverStatusModel: ServerStatusModel,
    val detailedPlayerModel: DetailedPlayerModel,
    val playerPunishments: List<PunishmentModel>,
)

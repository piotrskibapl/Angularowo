package pl.piotrskiba.angularowo.main.mainscreen.model

import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayer
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.punishment.model.toPunishmentBannerData

data class MainScreenData(
    val serverData: MainScreenServerData,
    val player: DetailedPlayer,
    val punishmentBanners: List<PunishmentBannerData>,
)

fun MainScreenDataModel.toUi() =
    MainScreenData(
        serverData = serverStatusModel.toUi(detailedPlayerModel.rank.staff),
        player = detailedPlayerModel.toUi(),
        punishmentBanners = playerPunishments.toPunishmentBannerData()
    )

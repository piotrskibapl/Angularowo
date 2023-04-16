package pl.piotrskiba.angularowo.main.mainscreen.model

import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayerData
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.punishment.model.toPunishmentBannerData

data class MainScreenData(
    val serverData: MainScreenServerData,
    val playerData: DetailedPlayerData,
    val punishmentBanners: List<PunishmentBannerData>,
)

fun MainScreenDataModel.toUi() =
    MainScreenData(
        serverData = serverStatusModel.toUi(detailedPlayerModel.rank.staff),
        playerData = detailedPlayerModel.toUi(),
        punishmentBanners = playerPunishments.toPunishmentBannerData()
    )

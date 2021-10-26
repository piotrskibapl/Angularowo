package pl.piotrskiba.angularowo.main.ban.model

import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel

// TODO: map the rest of data
data class BanBannerData(
    val username: String,
)

fun PunishmentModel.toUi() =
    BanBannerData(username)

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

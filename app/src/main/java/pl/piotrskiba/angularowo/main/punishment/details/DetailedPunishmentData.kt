package pl.piotrskiba.angularowo.main.punishment.details

import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import java.io.Serializable

data class DetailedPunishmentData(
    val id: String,
    val uuid: String?,
    val username: String,
    val reason: String,
    private val punishmentType: PunishmentType,
) : Serializable

fun PunishmentModel.toUi() =
    DetailedPunishmentData(
        id,
        uuid,
        username,
        reason,
        type,
    )

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

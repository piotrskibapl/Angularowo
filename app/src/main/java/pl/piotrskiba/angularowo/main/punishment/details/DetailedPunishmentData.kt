package pl.piotrskiba.angularowo.main.punishment.details

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import java.io.Serializable

data class DetailedPunishmentData(
    val id: String,
    val uuid: String?,
    val username: String,
    val actorName: String,
    val reason: String,
    private val punishmentType: PunishmentType,
) : Serializable {

    fun type(context: Context) = when (punishmentType) {
        PunishmentType.BAN -> context.getString(R.string.punishment_type_ban)
        PunishmentType.MUTE -> context.getString(R.string.punishment_type_mute)
        PunishmentType.WARN -> context.getString(R.string.punishment_type_warn)
        PunishmentType.KICK -> context.getString(R.string.punishment_type_kick)
    }

    fun toPunishmentBannerData() =
        PunishmentBannerData(
            id,
            uuid,
            username,
            reason,
            punishmentType
        )
}

fun PunishmentModel.toUi() =
    DetailedPunishmentData(
        id,
        uuid,
        username,
        actorName,
        reason,
        type,
    )

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

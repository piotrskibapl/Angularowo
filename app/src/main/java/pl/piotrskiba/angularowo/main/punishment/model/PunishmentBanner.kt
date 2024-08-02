package pl.piotrskiba.angularowo.main.punishment.model

import android.content.Context
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import java.io.Serializable
import java.util.Locale

data class PunishmentBanner(
    val id: String,
    val uuid: String?,
    val username: String,
    private val reason: String,
    private val punishmentType: PunishmentTypeModel,
) : Serializable {

    fun reason(context: Context): String {
        val reasonTrimDelimiters = arrayOf(":", " (")
        val trimmedReason = reason.split(*reasonTrimDelimiters).first().lowercase(Locale.getDefault())
        return when (punishmentType) {
            PunishmentTypeModel.BAN -> context.getString(R.string.punishment_ban_description_format, trimmedReason)
            PunishmentTypeModel.MUTE -> context.getString(R.string.punishment_mute_description_format, trimmedReason)
            PunishmentTypeModel.WARN -> context.getString(R.string.punishment_warn_description_format, trimmedReason)
            PunishmentTypeModel.KICK -> context.getString(R.string.punishment_kick_description_format, trimmedReason)
        }
    }

    @DrawableRes
    fun punishmentIcon() =
        when (punishmentType) {
            PunishmentTypeModel.BAN -> R.drawable.ic_ban
            PunishmentTypeModel.KICK -> R.drawable.ic_kick
            PunishmentTypeModel.WARN -> R.drawable.ic_warning
            PunishmentTypeModel.MUTE -> R.drawable.ic_mute
        }
}

fun PunishmentModel.toPunishmentBanner() =
    PunishmentBanner(
        id,
        uuid,
        username,
        reason,
        type,
    )

fun List<PunishmentModel>.toPunishmentBanners() =
    map { it.toPunishmentBanner() }

package pl.piotrskiba.angularowo.main.punishment.model

import android.content.Context
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.utils.UrlUtils
import java.io.Serializable
import java.util.Locale

data class PunishmentBannerData(
    val id: String,
    val uuid: String?,
    val username: String,
    private val reason: String,
    private val punishmentType: PunishmentType,
) : Serializable {

    fun reason(context: Context): String {
        val reasonTrimDelimiters = arrayOf(":", " (")
        val trimmedReason = reason.split(*reasonTrimDelimiters).first().lowercase(Locale.getDefault())
        return when (punishmentType) {
            PunishmentType.BAN -> context.getString(R.string.punishment_ban_description_format, trimmedReason)
            PunishmentType.MUTE -> context.getString(R.string.punishment_mute_description_format, trimmedReason)
            PunishmentType.WARN -> context.getString(R.string.punishment_warn_description_format, trimmedReason)
            PunishmentType.KICK -> context.getString(R.string.punishment_kick_description_format, trimmedReason)
        }
    }

    fun userAvatarUrl(context: Context) = UrlUtils.buildAvatarUrl(uuid, true, context)

    @DrawableRes
    fun punishmentIcon() =
        when (punishmentType) {
            PunishmentType.BAN -> R.drawable.ic_ban
            PunishmentType.KICK -> R.drawable.ic_kick
            PunishmentType.WARN -> R.drawable.ic_warning
            PunishmentType.MUTE -> R.drawable.ic_mute
        }
}

fun PunishmentModel.toPunishmentBannerData() =
    PunishmentBannerData(
        id,
        uuid,
        username,
        reason,
        type,
    )

fun List<PunishmentModel>.toPunishmentBannerData() =
    map { it.toPunishmentBannerData() }

package pl.piotrskiba.angularowo.main.ban.model

import android.content.Context
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.utils.UrlUtils
import java.io.Serializable

data class PunishmentBannerData(
    val uuid: String?,
    val username: String,
    val reason: String,
    private val punishmentType: PunishmentType,
) : Serializable {

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

fun PunishmentModel.toUi() =
    PunishmentBannerData(
        uuid,
        username,
        reason,
        type,
    )

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

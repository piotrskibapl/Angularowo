package pl.piotrskiba.angularowo.main.ban.model

import android.content.Context
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.utils.UrlUtils

// TODO: map ban type icon
data class BanBannerData(
    val uuid: String?,
    val username: String,
    val reason: String,
) {

    fun userAvatarUrl(context: Context) = UrlUtils.buildAvatarUrl(uuid, true, context)
}

fun PunishmentModel.toUi() =
    BanBannerData(
        uuid,
        username,
        reason
    )

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

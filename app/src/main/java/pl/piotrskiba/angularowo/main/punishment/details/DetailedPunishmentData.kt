package pl.piotrskiba.angularowo.main.punishment.details

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DetailedPunishmentData(
    val id: String,
    val uuid: String?,
    val username: String,
    val actorName: String,
    val reason: String,
    private val created: Date,
    private val expires: Date,
    private val punishmentType: PunishmentTypeModel,
) : Serializable {

    fun startDate(context: Context): String {
        val dateFormat = SimpleDateFormat(context.getString(R.string.punishment_date_format), Locale.getDefault())
        return dateFormat.format(created)
    }

    fun endDate(context: Context): String =
        when (expires.time) {
            0L -> context.getString(R.string.punishment_expiration_never)
            else -> {
                val dateFormat = SimpleDateFormat(context.getString(R.string.punishment_date_format), Locale.getDefault())
                dateFormat.format(expires)
            }
        }

    fun type(context: Context) = when (punishmentType) {
        PunishmentTypeModel.BAN -> context.getString(R.string.punishment_type_ban)
        PunishmentTypeModel.MUTE -> context.getString(R.string.punishment_type_mute)
        PunishmentTypeModel.WARN -> context.getString(R.string.punishment_type_warn)
        PunishmentTypeModel.KICK -> context.getString(R.string.punishment_type_kick)
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
        created,
        expires,
        type,
    )

fun List<PunishmentModel>.toUi() =
    map { it.toUi() }

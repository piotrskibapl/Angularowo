package pl.piotrskiba.angularowo.data.punishment.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import java.util.Date

class PunishmentRemote(
    val name: String,
    val uuid: String?,
    val reason: String,
    val actor_name: String,
    val start: Long,
    val end: Long,
    val type: String
)

fun PunishmentRemote.toDomain() = PunishmentModel(
    name,
    uuid,
    reason,
    actor_name,
    Date(start),
    Date(end),
    type.toPunishmentType()
)

fun List<PunishmentRemote>.toDomain() = map { it.toDomain() }

private fun String.toPunishmentType() = PunishmentType.valueOf(this)

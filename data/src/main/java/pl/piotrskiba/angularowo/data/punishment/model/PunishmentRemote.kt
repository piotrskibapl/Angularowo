package pl.piotrskiba.angularowo.data.punishment.model

import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import java.util.Date
import java.util.Locale

class PunishmentRemote(
    val id: String,
    val name: String,
    val uuid: String?,
    val reason: String,
    val actor_name: String,
    val start: Long,
    val end: Long,
    val type: String
)

fun PunishmentRemote.toDomain() = PunishmentModel(
    id,
    name,
    uuid,
    reason,
    actor_name,
    Date(start),
    Date(end),
    type.toPunishmentType()
)

fun List<PunishmentRemote>.toDomain() = map { it.toDomain() }

private fun String.toPunishmentType() = PunishmentType.valueOf(this.uppercase(Locale.ROOT))

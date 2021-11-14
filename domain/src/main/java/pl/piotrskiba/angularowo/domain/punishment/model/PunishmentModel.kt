package pl.piotrskiba.angularowo.domain.punishment.model

import java.util.Date

data class PunishmentModel(
    val id: String,
    val username: String,
    val uuid: String?,
    val reason: String,
    val actorName: String,
    val created: Date,
    val expires: Date,
    val type: PunishmentType
)

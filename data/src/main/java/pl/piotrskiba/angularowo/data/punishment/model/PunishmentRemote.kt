package pl.piotrskiba.angularowo.data.punishment.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import java.util.Date

class PunishmentRemote(
    @field:SerializedName("name") val username: String,
    val uuid: String?,
    val reason: String,
    @field:SerializedName("banner") val actorName: String,
    @field:SerializedName("bantime") val banTime: Long,
    @field:SerializedName("expires") val expireDate: Long,
    val type: String
)

fun PunishmentRemote.toDomain() = PunishmentModel(
    username,
    uuid,
    reason,
    actorName,
    Date(banTime),
    Date(expireDate),
    type.toPunishmentType()
)

fun List<PunishmentRemote>.toDomain() = map { it.toDomain() }

private fun String.toPunishmentType() = PunishmentType.valueOf(this)
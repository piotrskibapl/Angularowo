package pl.piotrskiba.angularowo.data.punishment.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.punishment.model.Punishment
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import java.util.Date

class PunishmentData(
    @field:SerializedName("name") val username: String,
    val uuid: String?,
    val reason: String,
    @field:SerializedName("banner") val actorName: String,
    @field:SerializedName("bantime") val banTime: Long,
    @field:SerializedName("expires") val expireDate: Long,
    val type: String
)

fun PunishmentData.toDomain() = Punishment(
    username,
    uuid,
    reason,
    actorName,
    Date(banTime),
    Date(expireDate),
    type.toPunishmentType()
)

fun List<PunishmentData>.toDomain() = map { it.toDomain() }

private fun String.toPunishmentType() = PunishmentType.valueOf(this)
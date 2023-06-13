package pl.piotrskiba.angularowo.domain.punishment.model

import java.util.Locale

enum class PunishmentTypeModel {
    BAN, MUTE, WARN, KICK
}

fun List<PunishmentTypeModel>.toRemote() =
    joinToString(
        separator = ",",
        transform = { it.toString().lowercase(Locale.ROOT) },
    )

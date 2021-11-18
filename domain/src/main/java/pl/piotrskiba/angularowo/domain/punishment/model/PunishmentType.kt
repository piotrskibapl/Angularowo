package pl.piotrskiba.angularowo.domain.punishment.model

import java.util.Locale

enum class PunishmentType {
    BAN, MUTE, WARN, KICK
}

fun List<PunishmentType>.toRemote() =
    joinToString(
        separator = ",",
        transform = { it.toString().lowercase(Locale.ROOT) }
    )

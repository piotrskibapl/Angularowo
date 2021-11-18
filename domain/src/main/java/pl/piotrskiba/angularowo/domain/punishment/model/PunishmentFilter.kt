package pl.piotrskiba.angularowo.domain.punishment.model

import java.util.Locale

enum class PunishmentFilter {
    ACTIVE, EXPIRED, ALL
}

fun PunishmentFilter.toRemote() =
    toString().lowercase(Locale.ROOT)

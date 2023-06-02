package pl.piotrskiba.angularowo.domain.punishment.model

import java.util.Locale

enum class PunishmentFilterModel {
    ACTIVE, EXPIRED, ALL
}

fun PunishmentFilterModel.toRemote() =
    toString().lowercase(Locale.ROOT)

package pl.piotrskiba.angularowo.main.player.details.model

import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel

enum class PunishmentType {
    MUTE, KICK, WARN, BAN
}

fun PunishmentType.toDomain() =
    when (this) {
        PunishmentType.MUTE -> PunishmentTypeModel.MUTE
        PunishmentType.KICK -> PunishmentTypeModel.KICK
        PunishmentType.WARN -> PunishmentTypeModel.WARN
        PunishmentType.BAN -> PunishmentTypeModel.BAN
    }

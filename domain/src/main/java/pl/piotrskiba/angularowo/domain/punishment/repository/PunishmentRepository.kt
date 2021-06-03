package pl.piotrskiba.angularowo.domain.punishment.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.Punishment
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType

interface PunishmentRepository {

    fun getPunishments(
        apiKey: String,
        accessToken: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<Punishment>>

    fun getPlayerPunishments(
        apiKey: String,
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<Punishment>>
}

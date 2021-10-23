package pl.piotrskiba.angularowo.domain.punishment.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType

interface PunishmentRepository {

    fun getPunishments(
        accessToken: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<PunishmentModel>>

    fun getPlayerPunishments(
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<PunishmentModel>>
}

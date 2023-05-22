package pl.piotrskiba.angularowo.domain.punishment.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel

interface PunishmentRepository {

    fun getPunishments(
        accessToken: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilter,
    ): Single<List<PunishmentModel>>

    fun getPlayerPunishments(
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilter,
    ): Single<List<PunishmentModel>>

    fun mutePlayer(
        accessToken: String,
        uuid: String,
        reason: String,
        length: Long,
    ): Completable

    fun kickPlayer(
        accessToken: String,
        uuid: String,
        reason: String,
    ): Completable

    fun warnPlayer(
        accessToken: String,
        uuid: String,
        reason: String,
        length: Long,
    ): Completable

    fun banPlayer(
        accessToken: String,
        uuid: String,
        reason: String,
        length: Long,
    ): Completable
}

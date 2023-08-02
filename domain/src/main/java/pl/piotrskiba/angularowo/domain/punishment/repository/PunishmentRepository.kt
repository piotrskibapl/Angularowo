package pl.piotrskiba.angularowo.domain.punishment.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel

interface PunishmentRepository {

    fun getPunishments(
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>>

    fun getPlayerPunishments(
        username: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>>

    fun mutePlayer(
        uuid: String,
        reason: String,
        length: Long,
    ): Completable

    fun kickPlayer(
        uuid: String,
        reason: String,
    ): Completable

    fun warnPlayer(
        uuid: String,
        reason: String,
        length: Long,
    ): Completable

    fun banPlayer(
        uuid: String,
        reason: String,
        length: Long,
    ): Completable
}
